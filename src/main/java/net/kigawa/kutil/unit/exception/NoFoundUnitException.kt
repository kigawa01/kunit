package net.kigawa.kutil.unit.exception

@Suppress("unused")
class NoFoundUnitException: UnitException {
  constructor(unitClass: Class<*>, name: String?, message: String, cause: Throwable?):
          super(unitClass, name, message, cause)
  
  constructor(unitClass: Class<*>, name: String?, message: String): super(unitClass, name, message)
  constructor(unitClass: Class<*>, message: String, cause: Throwable?): super(unitClass, message, cause)
  constructor(unitClass: Class<*>, message: String): super(unitClass, message)
}