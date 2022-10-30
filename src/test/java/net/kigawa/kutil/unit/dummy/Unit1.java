package net.kigawa.kutil.unit.dummy;

import net.kigawa.kutil.unit.Unit;
import net.kigawa.kutil.unit.container.UnitContainer;
import net.kigawa.kutil.unit.dummy.parent.AllUnitInterface;

@Unit
public class Unit1 implements AllUnitInterface
{
    protected final Unit4 unitDummy;
    protected final UnitContainer unitContainer;

    public Unit1(Unit4 unitDummy, UnitContainer unitContainer) {
        this.unitDummy = unitDummy;
        this.unitContainer = unitContainer;
    }
}
