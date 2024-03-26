package net.kigawa.kutil.kunit.api.options

import net.kigawa.kutil.kunit.api.options.Option

interface RegisterOption: Option {
  fun match(clazz: Class<out Any>): Boolean
}