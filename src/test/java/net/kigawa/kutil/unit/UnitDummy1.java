package net.kigawa.kutil.unit;

public class UnitDummy1 implements Unit
{
    protected final UnitDummy unitDummy;
    protected final UnitContainer unitContainer;

    public UnitDummy1(UnitDummy unitDummy, UnitContainer unitContainer)
    {

        this.unitDummy = unitDummy;
        this.unitContainer = unitContainer;
    }
}
