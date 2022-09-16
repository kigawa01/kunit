package net.kigawa.kutil.unit;

public class UnitInfo<T extends Unit>
{
    private final Class<T> unitClass;
    private final UnitContainer unitContainer;

    public UnitInfo(Class<T> unitClass, UnitContainer unitContainer)
    {
        this.unitClass = unitClass;
        this.unitContainer = unitContainer;
    }
}
