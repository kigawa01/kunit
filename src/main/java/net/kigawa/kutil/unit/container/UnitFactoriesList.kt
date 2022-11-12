package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.factory.UnitFactory

class UnitFactoriesList {
    private val factories = mutableSetOf<UnitFactory>()

    @Synchronized
    fun last(predicate: (UnitFactory) -> Boolean): UnitFactory {
        return factories.last(predicate)
    }

    @Synchronized
    fun add(unitFactory: UnitFactory) {
        factories.add(unitFactory)
    }
}