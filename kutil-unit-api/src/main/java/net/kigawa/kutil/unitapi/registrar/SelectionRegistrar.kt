package net.kigawa.kutil.unitapi.registrar

interface SelectionRegistrar {
  fun selectRegister(unitClass: Class<out Any>): (()->Unit)?
}