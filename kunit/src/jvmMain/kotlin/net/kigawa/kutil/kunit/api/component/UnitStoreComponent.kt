package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.api.extention.UnitStore

interface UnitStoreComponent: ComponentHolder<UnitStore> {
  fun findStore(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitStore
}