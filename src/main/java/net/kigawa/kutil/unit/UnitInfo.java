package net.kigawa.kutil.unit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class UnitInfo<T extends Unit>
{
    private final Class<T> unitClass;
    private final UnitContainer unitContainer;
    private final List<UnitInfo<?>> parents = new LinkedList<>();
    private final List<UnitInfo<?>> childs = new LinkedList<>();
    private T unit;

    public UnitInfo(Class<T> unitClass, UnitContainer unitContainer)
    {
        this.unitClass = unitClass;
        this.unitContainer = unitContainer;
    }

    public T getUnit(UnitInfo<?> parent) throws InvocationTargetException, InstantiationException, IllegalAccessException
    {
        parents.add(parent);
        parent.childs.add(this);
        if (unit != null) return unit;

        var constructor = getConstructor();
        var args = getArgs(constructor);

        return (T) constructor.newInstance(args);
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
