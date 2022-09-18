package net.kigawa.kutil.unit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class UnitInfo<T extends Unit>
{
    protected final Set<UnitInfo<?>> parents = new HashSet<>();
    protected final Set<UnitInfo<?>> childs = new HashSet<>();
    protected final boolean isResident;
    private final Class<T> unitClass;
    private final UnitContainer unitContainer;
    protected T unit;
    protected boolean shutdown;

    public UnitInfo(Class<T> unitClass, UnitContainer unitContainer, boolean isResident)
    {
        this.unitClass = unitClass;
        this.unitContainer = unitContainer;
        this.isResident = isResident;
    }

    protected void shutdown()
    {
        if (shutdown) return;
        shutdown = false;
        for (var child : childs) {
            child.shutdown();
        }
        unit.shutdown();
        for (var parent : parents) {
            parent.childs.remove(this);
            if (parent.childs.size() == 0 && !parent.isResident) parent.shutdown();
        }
    }

    public T getUnit(UnitInfo<?> child) throws InvocationTargetException, InstantiationException, IllegalAccessException
    {
        shutdown = true;
        if (child != null) {
            childs.add(child);
            child.parents.add(this);
        }
        if (unit != null) return unit;

        var constructor = getConstructor();
        var args = getArgs(constructor);

        unit = (T) constructor.newInstance(args);
        return unit;
    }

    private Object[] getArgs(Constructor<?> constructor)
    {
        var args = constructor.getParameterTypes();
        Object[] objects = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            objects[i] = unitContainer.getUnit(this, args[i]);
        }
        return objects;
    }

    private Constructor<?> getConstructor()
    {
        var constructors = unitClass.getConstructors();
        if (constructors.length == 1) return constructors[0];
        for (var constructor : constructors) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }

        throw new UnitException("cold not get constructor");
    }
}
