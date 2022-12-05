package net.kigawa.kutil.unit.extension.closer

interface UnitCloser {
    fun closeUnit(unit: Any)
    fun isValid(unit: Any): Boolean
}