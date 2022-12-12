package net.kigawa.kutil.unit.component.register

import net.kigawa.kutil.unit.extension.classlist.ClassList

interface UnitRegister {
  
  fun registerUnits(classList: ClassList): MutableList<Throwable>
  fun registerUnit(unitClass: Class<*>, name: String?): MutableList<Throwable>
  fun registerUnit(unitClass: Class<*>): MutableList<Throwable> {
    return registerUnit(unitClass, null)
  }
  
}