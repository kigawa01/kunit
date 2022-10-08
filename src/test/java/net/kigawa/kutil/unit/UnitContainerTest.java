package net.kigawa.kutil.unit;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

class UnitContainerTest extends Assertions
{
//    @Test
//    void registerUnit()
//    {
//        var con = UnitContainer.defaultContainer;
//        con.registerUnit(UnitDummy.class);
//        con.registerUnit(UnitDummy1.class);
//
//        assertTrue(con.unitMap.containsKey(UnitDummy.class));
//        assertTrue(con.unitMap.containsKey(UnitDummy1.class));
//
//    }
//
//    @Test
//    void registerResidentUnit()
//    {
//        var con = UnitContainer.defaultContainer;
//        con.registerResidentUnit(UnitDummy.class);
//        con.registerResidentUnit(UnitDummy1.class);
//
//        assertTrue(con.unitMap.get(UnitDummy.class).isResident);
//        assertTrue(con.unitMap.get(UnitDummy1.class).isResident);
//    }
//
//    @Test
//    void start()
//    {
//        var con = UnitContainer.defaultContainer;
//        con.registerResidentUnit(UnitDummy1.class);
//        con.registerUnit(UnitDummy.class);
//
//        con.start();
//
//        assertNotNull(con.residentMap.get(UnitDummy1.class));
//        assertEquals(2,con.unitMap.get(UnitDummy1.class).parents.size());
//        assertEquals(0,con.unitMap.get(UnitDummy1.class).childs.size());
//        assertEquals(0,con.unitMap.get(UnitDummy.class).parents.size());
//        assertEquals(1,con.unitMap.get(UnitDummy.class).childs.size());
//
//        var dummy1 = con.getResidentUnit(UnitDummy1.class);
//
//        assertNotNull(con.unitMap.get(UnitDummy.class).unit);
//        assertNotNull(con.unitMap.get(UnitDummy1.class).unit);
//        assertNotNull(dummy1);
//        assertNotNull(dummy1.unitContainer);
//        assertNotNull(dummy1.unitDummy);
//        assertEquals(dummy1.unitDummy, con.getUnit(con.unitMap.get(con.getClass()), UnitDummy.class));
//    }
//
//    @Test
//    void shutdownUnit()
//    {
//        var con = UnitContainer.defaultContainer;
//        con.registerUnit(UnitDummy.class);
//        con.registerResidentUnit(UnitDummy1.class);
//
//        con.start();
//        con.shutdownUnit(UnitDummy1.class);
//
//        System.out.println(con.residentMap);
//        assertEquals(0, con.residentMap.size());
//
//        con.start();
//        con.shutdownUnit(UnitDummy.class);
//
//        assertEquals(0, con.residentMap.size());
//    }
//
//    @Test
//    void shutdown()
//    {
//        var con = UnitContainer.defaultContainer;
//        con.registerUnit(UnitDummy.class);
//        con.registerResidentUnit(UnitDummy1.class);
//
//        con.start();
//        con.shutdown();
//
//        assertEquals(0, con.residentMap.size());
//    }

    public static void main(String[] args) {
        for (var cla : ArrayList.class.getInterfaces()) {
            System.out.println(cla);
        }
    }
}