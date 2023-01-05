package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.api.component.InitializedFilterComponent
import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.extension.database.ComponentInfoDatabase

class InitializedFilterComponentImpl(database: ComponentInfoDatabase):
  InitializedFilterComponent, ComponentHolderImpl<InitializedFilter>(database) {
  override fun <T> filter(obj: T): T {
    TODO("Not yet implemented")
  }
}