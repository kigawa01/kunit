package net.kigawa.kutil.unitimpl.registrar

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.container.UnitContainer
import net.kigawa.kutil.unitapi.component.UnitDatabaseComponent
import net.kigawa.kutil.unitapi.component.UnitStoreComponent
import net.kigawa.kutil.unitapi.options.RegisterOptionEnum
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.registrar.SelectionRegistrar
import net.kigawa.kutil.unitapi.util.AnnotationUtil

@LateInit
class SelectionRegistrarImpl(
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent, container: UnitContainer,
) : AbstractRegister(getterComponent, databaseComponent, container), SelectionRegistrar {
  override fun selectRegister(unitClass: Class<out Any>): (() -> Unit)? {
    if (!AnnotationUtil.hasUnitAnnotation(unitClass)) return null
    val result = registerTask(
      UnitIdentify(unitClass, AnnotationUtil.getUnitNameByAnnotation(unitClass)),
      RegisterOptions(*RegisterOptionEnum.getOption(unitClass))
    )
    return { result.invoke() }
  }
}