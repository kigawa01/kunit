package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.RegisterOptions
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.DefaultRegisterOption
import net.kigawa.kutil.unit.util.AnnotationUtil

open class SelectionRegistrar(
  getterComponent: UnitGetterComponent, databaseComponent: UnitDatabaseComponent, container: UnitContainer,
): AbstractRegister(getterComponent, databaseComponent, container) {
  fun selectRegister(unitClass: Class<out Any>): (()->Unit)? {
    if (!AnnotationUtil.hasUnitAnnotation(unitClass)) return null
   return registerTask(
      UnitIdentify(unitClass, AnnotationUtil.getUnitNameByAnnotation(unitClass)),
      RegisterOptions(*DefaultRegisterOption.getOption(unitClass))
    )
  }
}