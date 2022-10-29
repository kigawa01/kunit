package net.kigawa.kutil.unit;

import java.lang.reflect.Constructor;

public class UnitInfo
{
    protected final Class<?> unitClass;
    protected Object unit;

    public UnitInfo(Class<?> unitClass) {
        this.unitClass = unitClass;
    }


    protected Constructor<?> getConstructor() throws UnitException {
        var constructors = unitClass.getConstructors();
        if (constructors.length == 1) return constructors[0];
        for (var constructor : constructors) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }

        throw new UnitException("could not get constructor: " + unitClass);
    }
}
