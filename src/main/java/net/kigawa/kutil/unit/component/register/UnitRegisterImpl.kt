package net.kigawa.kutil.unit.component.register

import net.kigawa.kutil.unit.component.container.UnitIdentify
import net.kigawa.kutil.unit.component.database.UnitInfo
import net.kigawa.kutil.unit.extension.classlist.ClassList

class UnitRegisterImpl: UnitRegister {
  
  override fun registerUnits(classList: ClassList): MutableList<Throwable> {
    val errors = mutableListOf<Throwable>()
    errors.addAll(classList.errors)
    
    classList.classes.forEach {
      try {
        registerUnit(it, null)
      } catch (e: Throwable) {
        errors.add(e)
      }
    }
    
    return errors
  }
  
  override fun registerUnit(unitClass: Class<*>, name: String?): MutableList<Throwable> {
    val unitIdentify = UnitIdentify(unitClass, name)
    if (unitDatabase.contain(unitIdentify)) return mutableListOf()
    val info = UnitInfo(unitIdentify)
    
    val errors = mutableListOf<Throwable>()
    val factory = factories.last {
      try {
        it.isValid(unitIdentify)
      } catch (e: Throwable) {
        errors.add(e)
        false
      }
    } ?: return errors
    info.loaded(factory)
    
    try {
      unitDatabase.register(info)
    } catch (e: Throwable) {
      errors.add(e)
    }
    
    return errors
  }
  
}