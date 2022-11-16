package net.kigawa.kutil.unit.closer

import net.kigawa.kutil.unit.util.Util

class AutoCloseAbleCloser : UnitCloser {
    override fun closeUnit(unit: Any) {
        if (!isValid(unit)) return
        (unit as AutoCloseable).close()
    }

    override fun isValid(unit: Any): Boolean {
        return Util.instanceOf(unit.javaClass, AutoCloseable::class.java)
    }
}