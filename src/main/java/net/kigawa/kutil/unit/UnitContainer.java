package net.kigawa.kutil.unit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UnitContainer
{
    private final Package rootPackage;
    private final Map<Class<?>, UnitInfo> unitInfoMap = new HashMap<>();
    private final Map<Class<?>, Class<?>> interfaceMap = new HashMap<>();

    public UnitContainer(Class<?> rootClass) {
        rootPackage = rootClass.getPackage();

        var containerInfo = new UnitInfo(getClass());
        containerInfo.unit = this;
        unitInfoMap.put(getClass(), containerInfo);

        loadUnits();
        initUnits();
    }

    public <T> T getUnit(Class<T> unitClass) {
        var result = unitInfoMap.get(unitClass);
        if (result == null) return getUnitByInterface(unitClass);
        return (T) result.unit;
    }

    private <T> T getUnitByInterface(Class<T> interfaceClass) {
        var mappedClass = interfaceMap.get(interfaceClass);
        if (mappedClass != null) return getUnit(interfaceClass);

        T result = null;
        for (var unitClass : unitInfoMap.keySet()) {
            if (!Arrays.asList(unitClass.getInterfaces()).contains(interfaceClass)) continue;

            if (result != null) throw new UnitException("interface must implemented by only one unit");

            interfaceMap.put(interfaceClass, unitClass);
            result = (T) getUnit(unitClass);
        }

        if (result == null) throw new UnitException("unit is not found");
        return result;
    }

    private void initUnits() {
        for (var unitClass : unitInfoMap.keySet()) {
            loadUnit(unitClass);
        }
    }

    private void loadUnit(Class<?> unitClass) {
        var unitInfo = unitInfoMap.get(unitClass);
        if (unitInfo.unit != null) return;

        var constructor = unitInfo.getConstructor();

        var parameters = constructor.getParameterTypes();
        var objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            loadUnit(parameters[i]);
            objects[i] = getUnit(parameters[i]);
        }
        try {
            unitInfo.unit = constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new UnitException("cold not init unit", e);
        }
    }

    private void loadUnits() {
        final String resourceName = rootPackage.getName().replace('.', '/');
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL root = classLoader.getResource(resourceName);

        if (root == null) throw new UnitException("cold not load units classes");

        if ("file".equals(root.getProtocol())) {
            var files = new File(root.getFile()).listFiles((dir, name) -> name.endsWith(".class"));
            if (files == null) throw new UnitException("cold not load unit files");

            for (var file : files) {
                var name = file.getName();
                name = name.replaceAll(".class$", "");
                name = rootPackage.getName() + "." + name;

                try {
                    loadClass(Class.forName(name));
                } catch (ClassNotFoundException e) {
                    throw new UnitException("cold not load unit", e);
                }
            }
            return;
        }

        if ("jar".equals(root.getProtocol())) {
            try (var jarFile = ((JarURLConnection) root.openConnection()).getJarFile()) {
                for (var entry : Collections.list(jarFile.entries())) {
                    var name = entry.getName();
                    if (!name.startsWith(resourceName)) continue;
                    if (!name.endsWith(".class")) continue;
                    name = name.replace('/', '.').replaceAll(".class$", "");

                    loadClass(Class.forName(name));
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new UnitException("cold not load unit", e);
            }
        }
    }

    private void loadClass(Class<?> clazz) {
        if (clazz.getAnnotation(Unit.class) == null) return;
        unitInfoMap.put(clazz, new UnitInfo(clazz));
    }
}
