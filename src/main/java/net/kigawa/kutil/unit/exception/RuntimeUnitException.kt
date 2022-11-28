package net.kigawa.kutil.unit.exception

import net.kigawa.kutil.unit.UnitInfo

open class RuntimeUnitException: RuntimeException {
  constructor(unitClass: Class<*>, name: String?, message: String, cause: Throwable?): super(
    "$message\n" +
    "\tclass: ${unitClass.name}\n" +
    "\tname: $name",
    cause
  )
  
  constructor(unitClass: Class<*>, name: String?, message: String):
          this(unitClass, name, message, null)
  
  constructor(unitClass: Class<*>, message: String, cause: Throwable?):
          this(unitClass, null, message, cause)
  
  constructor(unitClass: Class<*>, message: String):
          this(unitClass, null, message, null)
  
  constructor(unitInfo: UnitInfo, message: String, cause: Throwable?):
          this(unitInfo.unitClass, unitInfo.name, message, cause)
}