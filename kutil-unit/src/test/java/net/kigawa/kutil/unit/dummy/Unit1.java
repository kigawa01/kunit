package net.kigawa.kutil.unit.dummy;

import net.kigawa.kutil.unit.dummy.parent.*;
import net.kigawa.kutil.unitapi.annotation.*;
import net.kigawa.kutil.unitapi.component.*;

@Kunit
public class Unit1 implements UnitOneToFourInterface
{
    protected final Unit4 unitDummy;
    protected final UnitContainer unitContainer;

    public Unit1(Unit4 unitDummy, UnitContainer unitContainer)
    {
        this.unitDummy = unitDummy;
        this.unitContainer = unitContainer;
    }
}
