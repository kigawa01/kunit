package net.kigawa.kutil.unitapi.options

import net.kigawa.kutil.unitapi.options.Option

interface RegisterOption: Option {
  fun match(clazz: Class<out Any>): Boolean
}