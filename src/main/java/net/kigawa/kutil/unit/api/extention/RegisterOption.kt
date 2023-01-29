package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.util.Option

interface RegisterOption: Option {
  fun match(clazz: Class<out Any>): Boolean
}