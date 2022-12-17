package net.kigawa.kutil.unit.exception

import net.kigawa.kutil.unit.component.info.UnitInfo

@Suppress("unused")
class NoFoundUnitException: UnitException {
  constructor(unitClass: Class<*>, name: String?, message: String, cause: Throwable?):
          super(unitClass, name, message, cause)
  
  constructor(unitClass: Class<*>, name: String?, message: String): super(unitClass, name, message)
  constructor(unitClass: Class<*>, message: String, cause: Throwable?): super(unitClass, message, cause)
  constructor(unitClass: Class<*>, message: String): super(unitClass, message)
  constructor(unitInfo: UnitInfo, message: String, cause: Throwable?): super(unitInfo, message, cause)
  constructor(unitClass: UnitInfo, message: String): super(unitClass, message)
}