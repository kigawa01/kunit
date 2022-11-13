package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.factory.UnitFactory
import java.util.concurrent.FutureTask

class UnitInfo(val unitClass: Class<*>, val name: String?) {
    var status: UnitStatus = UnitStatus.NOT_LOADED
        private set

    var unit: Any? = null
        set(value) {
            field = value
            status = UnitStatus.INITIALIZED
        }
    var future: FutureTask<*>? = null
        set(value) {
            field = value
            status = UnitStatus.INITIALIZING
        }
    var factory: UnitFactory? = null
        set(value) {
            field = value
            status = if (field == null) UnitStatus.NOT_LOADED
            else UnitStatus.LOADED
        }

    constructor(unitClass: Class<*>) : this(unitClass, null)

}