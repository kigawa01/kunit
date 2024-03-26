package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.component.InitializedFilterComponent
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.extention.InitializedFilter

class InitializedFilterComponentImpl(
  database: ComponentDatabase,
  container: UnitContainer,
) :
  InitializedFilterComponent,
  net.kigawa.kutil.kunit.impl.component.ComponentHolderImpl<InitializedFilter>(container, database) {
  override fun <T : Any> filter(obj: T, stack: InitStack): T {
    var result = obj

    forEach {
      result = it.filter(result, stack)
    }

    return result
  }
}