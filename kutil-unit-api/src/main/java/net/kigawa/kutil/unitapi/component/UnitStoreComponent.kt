package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.extention.UnitStore

interface UnitStoreComponent: ComponentHolder<UnitStore> {
  fun findStore(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitStore
}