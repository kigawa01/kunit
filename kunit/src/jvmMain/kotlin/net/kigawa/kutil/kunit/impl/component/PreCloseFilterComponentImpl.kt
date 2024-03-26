package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.PreCloseFilterComponent
import net.kigawa.kutil.kunit.api.component.UnitInfo
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.extention.PreCloseFilter

@LateInit
class PreCloseFilterComponentImpl(
  container: UnitContainer,
  database: ComponentDatabase,
) : PreCloseFilterComponent, net.kigawa.kutil.kunit.impl.component.ComponentHolderImpl<PreCloseFilter>(container, database) {

  override fun filter(info: UnitInfo<out Any>) {
    forEach {
      it.filter(info)
    }
  }
}