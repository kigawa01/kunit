package net.kigawa.kutil.kunit.impl.registrar

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.kunit.api.component.UnitStoreComponent
import net.kigawa.kutil.kunit.api.options.RegisterOptionEnum
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.api.registrar.SelectionRegistrar
import net.kigawa.kutil.kunit.api.util.AnnotationUtil

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