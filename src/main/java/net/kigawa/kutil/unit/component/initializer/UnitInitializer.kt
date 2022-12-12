package net.kigawa.kutil.unit.component.initializer

import java.util.concurrent.*

interface UnitInitializer {
  fun <T> initUnits(unitClass: Class<T>, name: String?): MutableList<Throwable>
  
  fun initUnits(): MutableList<Throwable> {
    return initUnits(Object::class.java)
  }
  
  fun <T> initUnits(unitClass: Class<T>): MutableList<Throwable> {
    return initUnits(unitClass, null)
  }
  
  fun <T> initUnitsAsync(unitClass: Class<T>, name: String?): FutureTask<MutableList<Throwable>>
}