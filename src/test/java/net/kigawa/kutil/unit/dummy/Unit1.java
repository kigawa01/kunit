package net.kigawa.kutil.unit.dummy;

import net.kigawa.kutil.unit.annotation.*;
import net.kigawa.kutil.unit.container.*;
import net.kigawa.kutil.unit.dummy.parent.*;
@Unit
public class Unit1 implements AllUnitInterface
{
    protected final Unit4 unitDummy;
    protected final UnitContainerImpl unitContainer;

    public Unit1(Unit4 unitDummy, UnitContainerImpl unitContainer)
    {
        this.unitDummy = unitDummy;
        this.unitContainer = unitContainer;
    }
}
