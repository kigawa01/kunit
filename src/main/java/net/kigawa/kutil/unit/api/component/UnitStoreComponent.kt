package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.api.extention.UnitStore
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.api.extention.RegisterOptions

interface UnitStoreComponent: ComponentHolder<UnitStore> {
  fun findStore(identify: UnitIdentify<out Any>, options: RegisterOptions): UnitStore
}