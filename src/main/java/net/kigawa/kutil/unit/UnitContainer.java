package net.kigawa.kutil.unit;

import java.util.HashMap;
import java.util.Map;

public abstract class UnitContainer implements Unit
{
    protected Map<Class<? extends Unit>, UnitInfo> unitMap = new HashMap<>();

    public <T extends Unit> T getResidentUnit(Class<T> unitClass)
    {
        return getUnit(this, unitClass);
    }

    public <T extends Unit> T getUnit(Unit parent, Class<T> unitClass)
    {
        // TODO: 2022/09/10
        return null;
    }

    public <T extends Unit> void shutdown(Class<T> unitClass){
        // TODO: 2022/09/10
    }

    public void shutdown()
    {
        // TODO: 2022/09/10
    }
}
