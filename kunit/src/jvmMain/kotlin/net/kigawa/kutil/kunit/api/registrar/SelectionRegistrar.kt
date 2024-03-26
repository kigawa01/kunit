package net.kigawa.kutil.kunit.api.registrar

interface SelectionRegistrar {
  fun selectRegister(unitClass: Class<out Any>): (()->Unit)?
}