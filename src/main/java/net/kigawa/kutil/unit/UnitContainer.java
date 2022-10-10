package net.kigawa.kutil.unit;

import kotlin.Metadata;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.util.*;

public class UnitContainer
{
    private final Map<Class<?>, UnitInfo> unitInfoMap = new HashMap<>();
    private final Map<Class<?>, Class<?>> interfaceMap = new HashMap<>();
    private final UnitContainer parent;

    public UnitContainer(Object... units) {
        this(null, units);
    }

    public UnitContainer(UnitContainer parent, Object... units) {
        this.parent = parent;
        registerUnit(this);

        for (Object unit : units) {
            registerUnit(unit);
        }
    }

    public void loadUnits(Class<?> rootClass) throws UnitException {
        registerUnits(rootClass);
        initUnits();
    }

    public <T> void registerUnit(T unit) {
        var containerInfo = new UnitInfo(unit.getClass());
        containerInfo.unit = unit;
        unitInfoMap.put(unit.getClass(), containerInfo);
    }

    public <T> T getUnit(Class<T> unitClass) {
        var unitInfo = unitInfoMap.get(unitClass);
        if (unitInfo != null) return (T) unitInfo.unit;

        var result = getUnitByInterface(unitClass);
        if (result != null) return result;

        if (parent != null) return parent.getUnit(unitClass);

        throw new RuntimeUnitException("unit is not found: " + unitClass);
    }

    private <T> T getUnitByInterface(Class<T> interfaceClass) {
        var mappedClass = interfaceMap.get(interfaceClass);
        if (mappedClass != null) return getUnit(interfaceClass);

        Class<?> resultClass = null;
        for (var unitClass : unitInfoMap.keySet()) {
            if (!Arrays.asList(unitClass.getInterfaces()).contains(interfaceClass)) continue;

            if (resultClass != null)
                throw new RuntimeUnitException("interface must implemented by only one unit: " + resultClass + unitClass);

            resultClass = unitClass;
        }

        if (resultClass == null) return null;
        interfaceMap.put(interfaceClass, resultClass);
        return (T) getUnit(resultClass);
    }

    private void initUnits() throws UnitException {
        var exceptions = new LinkedList<Exception>();
        for (var unitClass : unitInfoMap.keySet()) {
            try {
                initUnit(unitClass);
            } catch (Exception e) {
                exceptions.add(new UnitException("could not init unit: " + unitClass, e));
            }
        }
        throwExceptions(exceptions, new UnitException("there are exceptions when init units"));
    }

    private void initUnit(Class<?> unitClass) throws UnitException {
        var unitInfo = unitInfoMap.get(unitClass);
        if (unitInfo.unit != null) return;

        if (unitClass.isAnnotationPresent(Metadata.class)) {
            unitInfo.unit = initKotlinClass(unitClass, unitInfo);
            return;
        }
        unitInfo.unit = initNormalClass(unitClass, unitInfo);
    }

    private Object initKotlinClass(Class<?> unitClass, UnitInfo unitInfo) throws UnitException {
        try {
            var field = unitClass.getField("INSTANCE");

            return field.get(null);
        } catch (NoSuchFieldException e) {
            return initNormalClass(unitClass, unitInfo);
        } catch (IllegalAccessException e) {
            throw new UnitException("could not access INSTANCE field: ", e);
        }
    }

    private Object initNormalClass(Class<?> unitClass, UnitInfo unitInfo) throws UnitException {
        var constructor = unitInfo.getConstructor();

        var parameters = constructor.getParameterTypes();
        var objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            initUnit(parameters[i]);
            objects[i] = getUnit(parameters[i]);
        }
        try {
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnitException("could not init unit: " + unitClass, e);
        } catch (InvocationTargetException e) {
            throw new UnitException("could not init unit: " + unitClass, e.getCause());
        }
    }

    private void registerUnits(Class<?> rootClass) throws UnitException {
        interfaceMap.clear();
        var rootPackage = rootClass.getPackage();
        var resourceName = rootPackage.getName().replace('.', '/');
        var classLoader = rootClass.getClassLoader();
        var root = classLoader.getResource(resourceName);
        var exceptions = new LinkedList<Exception>();

        if (root == null) throw new UnitException("could not load class files");

        if ("file".equals(root.getProtocol())) {
            var files = new File(root.getFile()).listFiles((dir, name) -> name.endsWith(".class"));
            if (files == null) throw new UnitException("cold not load unit files");

            for (var file : files) {
                var name = file.getName();
                name = name.replaceAll(".class$", "");
                name = rootPackage.getName() + "." + name;

                try {
                    registerUnit(Class.forName(name));
                } catch (Exception e) {
                    exceptions.add(new UnitException("cold not load unit: " + name, e));
                }
            }
        } else if ("jar".equals(root.getProtocol())) {
            try (var jarFile = ((JarURLConnection) root.openConnection()).getJarFile()) {
                for (var entry : Collections.list(jarFile.entries())) {
                    var name = entry.getName();
                    if (!name.startsWith(resourceName)) continue;
                    if (!name.endsWith(".class")) continue;
                    name = name.replace('/', '.').replaceAll(".class$", "");

                    try {
                        registerUnit(Class.forName(name));
                    } catch (Exception e) {
                        exceptions.add(new UnitException("could not load unit: " + name, e));
                    }
                }
            } catch (IOException e) {
                throw new UnitException("could not load units file", e);
            }
        }
        throwExceptions(exceptions, new UnitException("there are exceptions when load units"));
    }

    private void registerUnit(Class<?> unitClass) {
        if (unitClass.getAnnotation(Unit.class) == null) return;
        unitInfoMap.put(unitClass, new UnitInfo(unitClass));
    }

    private <E extends Exception> void throwExceptions(List<Exception> exceptions, E base) throws E {
        if (exceptions.size() == 0) return;

        for (var e : exceptions) {
            base.addSuppressed(e);
        }
        throw base;
    }
}
