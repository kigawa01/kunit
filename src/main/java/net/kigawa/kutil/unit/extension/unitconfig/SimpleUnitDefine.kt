package net.kigawa.kutil.unit.extension.unitconfig

class SimpleUnitDefine(override val classes: MutableList<UnitConfig>): UnitConfigs {
  override val errors: MutableList<Throwable>
    get() = mutableListOf()
}