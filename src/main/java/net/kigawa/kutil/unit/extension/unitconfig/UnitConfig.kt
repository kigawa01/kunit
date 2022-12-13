package net.kigawa.kutil.unit.extension.unitconfig

import net.kigawa.kutil.unit.component.register.UnitRegister
import net.kigawa.kutil.unit.extension.closer.UnitCloser
import net.kigawa.kutil.unit.extension.database.UnitInfoDatabase
import net.kigawa.kutil.unit.extension.factory.UnitFactory
import net.kigawa.kutil.unit.extension.getter.UnitGetter

class UnitConfig(
  val unitClass: Class<*>,
  var name: String? = null,
  var register: UnitRegister? = null,
  var getter: UnitGetter? = null,
  var factory: UnitFactory? = null,
  var database: UnitInfoDatabase? = null,
  var closers: MutableList<UnitCloser> = mutableListOf(),
) {
  constructor(unitClass: Class<*>, name: String?): this(unitClass, name, null)
  constructor(unitClass: Class<*>): this(unitClass, null)
}