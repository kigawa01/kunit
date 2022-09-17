package net.kigawa.kutil.unit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class UnitContainer implements Unit
{
    protected Map<Class<? extends Unit>, UnitInfo<?>> unitMap = new HashMap<>();

    public UnitContainer()
    {
        unitMap.put(getClass(), new UnitInfo<>(getClass(), this, true));
    }

    public <T extends Unit> void registerUnit(Class<T> unitClass)
    {
        var unitInfo = new UnitInfo<>(unitClass, this, false);
        unitMap.put(unitClass, unitInfo);
    }

    public <T extends Unit> void registerResidentUnit(Class<T> unitClass)
    {
        registerUnit(unitClass);
        var unitInfo = unitMap.get(getClass());
        if (unitInfo == null) throw new UnitException("unit is not exist");
        getUnit(unitInfo, unitClass);
    }

    protected <T extends Unit> T getUnit(UnitInfo<?> parent, Class<?> unitClass)
    {
        try {
            return (T) unitMap.get(unitClass)
                    .getUnit(parent);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends Unit> void shutdownUnit(Class<T> unitClass)
    {
        unitMap.get(unitClass).shutdown();
    }

    @Override
    public void shutdown()
    {
        shutdownUnit(getClass());
    }
}
