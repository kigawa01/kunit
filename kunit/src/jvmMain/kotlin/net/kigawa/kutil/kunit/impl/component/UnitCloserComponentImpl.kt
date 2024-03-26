package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.PreCloseFilterComponent
import net.kigawa.kutil.kunit.api.component.UnitCloserComponent
import net.kigawa.kutil.kunit.api.component.UnitInfo
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.extention.UnitCloser

@LateInit
class UnitCloserComponentImpl(
  container: UnitContainer,
  database: ComponentDatabase,
  private val closeFilterComponent: PreCloseFilterComponent,
) : UnitCloserComponent,
    ComponentHolderImpl<UnitCloser>(container, database) {
  override fun closeUnit(info: UnitInfo<out Any>) {
    closeFilterComponent.filter(info)
    last {
      it.closeUnit(info)
    }
  }
}