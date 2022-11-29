package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.factory.UnitFactory
import java.util.concurrent.FutureTask

class UnitInfo(unitIdentify: UnitIdentify) {
  val unitIdentify: UnitIdentify
  
  init {
    val name = if (unitIdentify.name == null || unitIdentify.name == "") {
      val unitAnnotation = unitIdentify.unitClass.getAnnotation(Unit::class.java)
      if (unitAnnotation == null || unitAnnotation.name == "") unitIdentify.unitClass.name
      else unitAnnotation.name
    } else unitIdentify.name
    
    this.unitIdentify = UnitIdentify(unitIdentify.unitClass, name)
  }
  
  private var status: UnitStatus = UnitStatus.NOT_LOADED
  
  var unit: Any? = null
    @Synchronized set(value) {
      field = value
      status = UnitStatus.INITIALIZED
    }
  var future: FutureTask<*>? = null
    @Synchronized set(value) {
      field = value
      status = UnitStatus.INITIALIZING
    }
  var factory: UnitFactory? = null
    @Synchronized set(value) {
      field = value
      status = if (field == null) UnitStatus.NOT_LOADED
      else UnitStatus.LOADED
    }
  
  @Synchronized
  fun <T> useStatus(function: (UnitStatus)->T): T {
   return function(status)
  }
}