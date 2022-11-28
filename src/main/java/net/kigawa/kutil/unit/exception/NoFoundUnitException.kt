package net.kigawa.kutil.unit.exception

class NoFoundUnitException: RuntimeUnitException {
  constructor(unitClass: Class<*>, message: String?, cause: Throwable?): super(unitClass, message, cause)
  constructor(unitClass: Class<*>, message: String?): super(unitClass, message)
  constructor(unitClass: Class<*>, name: String?, message: String?): super(unitClass, name, message)
}