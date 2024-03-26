package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.component.PreInitFilterComponent
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.extention.PreInitFilter

class PreInitFilterComponentImpl(
  container: UnitContainer,
  database: ComponentDatabase,
) :
  PreInitFilterComponent, ComponentHolderImpl<PreInitFilter>(container, database) {
  override fun <T : Any> filter(identify: UnitIdentify<T>, stack: InitStack) {
    forEach {
      it.filter(identify, stack)
    }
  }
}