package net.kigawa.kutil.unit.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptionEnum
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.registrar.SelectionRegistrar
import net.kigawa.kutil.unitapi.util.AnnotationUtil

abstract class SelectionRegistrarImpl(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent, container: UnitContainer,
): AbstractRegister(getterComponent, databaseComponent, container), SelectionRegistrar {
  override fun selectRegister(unitClass: Class<out Any>): (()->Unit)? {
    if (!AnnotationUtil.hasUnitAnnotation(unitClass)) return null
    return registerTask(
      UnitIdentify(unitClass, AnnotationUtil.getUnitNameByAnnotation(unitClass)),
      RegisterOptions(*RegisterOptionEnum.getOption(unitClass))
    )
  }
}