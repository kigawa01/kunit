package net.kigawa.kutil.unit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class UnitContainer implements Unit
{
    public static final UnitContainer defaultContainer = new UnitContainer();
    protected Map<Class<? extends Unit>, UnitInfo<?>> unitMap = new HashMap<>();
    protected Map<Class<? extends Unit>, Unit> residentMap = new HashMap<>();

    public UnitContainer()
    {
        unitMap.put(getClass(), new UnitInfo<>(getClass(), this, true));
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public <T extends Unit> T getResidentUnit(Class<T> unitClass)
    {
        var unit = residentMap.get(unitClass);
        if (unit == null) throw new UnitException("unit is not exits");

        return (T) unit;
    }

    public void start()
    {
        var conInfo = unitMap.get(getClass());
        for (var entry : unitMap.entrySet()) {
            if (entry.getValue().isResident) {
                try {
                    var unit = entry.getValue().getUnit(null);
                    conInfo.childs.add(entry.getValue());
                    residentMap.put(entry.getKey(), unit);
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public <T extends Unit> void registerUnit(Class<T> unitClass)
    {
        var unitInfo = new UnitInfo<>(unitClass, this, false);
        unitMap.put(unitClass, unitInfo);
    }

    public <T extends Unit> void registerResidentUnit(Class<T> unitClass)
    {
        var unitInfo = new UnitInfo<>(unitClass, this, true);
        unitMap.put(unitClass, unitInfo);
    }

    protected <T extends Unit> T getUnit(UnitInfo<?> parent, Class<?> unitClass)
    {
        try {
            return (T) unitMap.get(unitClass).getUnit(parent);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T extends Unit> void shutdownUnit(Class<T> unitClass)
    {
        unitMap.get(unitClass).shutdown();
        residentMap.remove(unitClass);
    }

    @Override
    public void shutdown()
    {
        shutdownUnit(getClass());
    }
}
