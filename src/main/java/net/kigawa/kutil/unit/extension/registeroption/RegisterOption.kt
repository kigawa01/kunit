package net.kigawa.kutil.unit.extension.registeroption

interface RegisterOption {
  fun match(clazz: Class<out Any>): Boolean
}