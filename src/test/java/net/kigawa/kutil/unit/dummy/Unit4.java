package net.kigawa.kutil.unit.dummy;

import net.kigawa.kutil.unit.annotation.*;
import net.kigawa.kutil.unit.dummy.parent.*;

@Dependency(value = Unit1.class)
@Dependency(value = Unit2.class)
@Unit
public class Unit4 extends AbstractUnit implements UnitInterface1, AllUnitInterface
{
    public Unit4()
    {
    }
}
