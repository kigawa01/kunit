package net.kigawa.kutil.unit.closer

interface UnitCloser {
    fun closeUnit(unit: Any)
    fun isValid(unit: Any): Boolean
}