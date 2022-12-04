package net.kigawa.kutil.unit.exception

import net.kigawa.kutil.unit.UnitIdentify
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
          this(unitInfo.identify, message, cause)
  
  constructor(identify: UnitIdentify, message: String, cause: Throwable?):
          this(identify.unitClass, identify.name, message, cause)
  
  constructor(unitInfo: UnitInfo, message: String):
          this(unitInfo, message, null)
}