package net.kigawa.kutil.unit.extension.identify

class SingleUnitIdentify(unitConfig: UnitIdentify<*>): UnitIdentifies {
  override val classes: MutableList<UnitIdentify<*>> = mutableListOf(unitConfig)
  override val errors: MutableList<Throwable>
    get() = mutableListOf()
}