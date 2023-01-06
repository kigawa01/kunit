package net.kigawa.kutil.unit.api.extention

interface RegisterOption {
  fun match(clazz: Class<out Any>): Boolean
}