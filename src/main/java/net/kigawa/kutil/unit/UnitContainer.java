package net.kigawa.kutil.unit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class UnitContainer implements Unit
{
    protected Map<Class<? extends Unit>, UnitInfo<?>> unitMap = new HashMap<>();

    public UnitContainer()
    {
        unitMap.put(getClass(), new UnitInfo<>(getClass(), this)
        {

        });
    }

    public <T extends Unit> void registerUnit(Class<T> unitClass)
    {
        var unitInfo = new UnitInfo<>(unitClass, this);
        unitMap.put(unitClass, unitInfo);
    }

    public <T extends Unit> T getResidentUnit(Class<T> unitClass)
    {
        return getUnit(unitMap.get(getClass()), unitClass);
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
        // TODO: 2022/09/10
    }

    public void shutdown()
    {
        // TODO: 2022/09/10
    }
}
