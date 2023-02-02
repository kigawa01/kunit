package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.util.Option

interface RegisterOption: Option {
  fun match(clazz: Class<out Any>): Boolean
}