package net.kigawa.kutil.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UnitContainerTest extends Assertions
{

    @Test
    void getUnit() {
        var con = new UnitContainer(getClass());

        assertNotNull(con.getUnit(UnitDummy.class));
        assertNotNull(con.getUnit(UnitDummy1.class));
        assertNotNull(con.getUnit(UnitDummyInterface.class));
        assertThrows(UnitException.class, () -> con.getUnit(AllUnitInterface.class));
    }
}