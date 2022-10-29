package net.kigawa.kutil.unit.dummy;

import net.kigawa.kutil.unit.dummy.parent.AbstractUnit;
import net.kigawa.kutil.unit.Unit;
import net.kigawa.kutil.unit.dummy.parent.AllUnitInterface;
import net.kigawa.kutil.unit.dummy.parent.UnitInterface1;

@Unit
public class Unit4 extends AbstractUnit implements UnitInterface1, AllUnitInterface
{
    public Unit4() {
    }
}
