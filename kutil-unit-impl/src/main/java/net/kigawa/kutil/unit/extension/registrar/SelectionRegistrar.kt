package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unitapi.options.RegisterOptionEnum
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.util.AnnotationUtil

open class SelectionRegistrar(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent, container: UnitContainer,
): AbstractRegister(getterComponent, databaseComponent, container) {
  fun selectRegister(unitClass: Class<out Any>): (()->Unit)? {
    if (!AnnotationUtil.hasUnitAnnotation(unitClass)) return null
   return registerTask(
     UnitIdentify(unitClass, AnnotationUtil.getUnitNameByAnnotation(unitClass)),
     RegisterOptions(*RegisterOptionEnum.getOption(unitClass))
    )
  }
}