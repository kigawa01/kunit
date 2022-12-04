package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.concurrent.ThreadBlocker
import net.kigawa.kutil.unit.exception.RuntimeUnitException
import net.kigawa.kutil.unit.factory.UnitFactory
import java.util.concurrent.TimeUnit

class UnitInfo(unitIdentify: UnitIdentify) {
  val identify: UnitIdentify
  private val initializedLock = ThreadBlocker()
  
  init {
    val name = if (unitIdentify.name == null || unitIdentify.name == "") {
      val unitAnnotation = unitIdentify.unitClass.getAnnotation(Unit::class.java)
      if (unitAnnotation == null || unitAnnotation.name == "") unitIdentify.unitClass.name
      else unitAnnotation.name
    } else unitIdentify.name
    
    this.identify = UnitIdentify(unitIdentify.unitClass, name)
  }
  
  var status: UnitStatus = UnitStatus.NOT_LOADED
    private set
  private var unit: Any? = null
  private var factory: UnitFactory? = null
  
  fun getFactory(): UnitFactory {
    if (status == UnitStatus.NOT_LOADED) throw RuntimeUnitException(this, "unit is not valid status\n\tstatus: $status")
    return factory ?: throw RuntimeUnitException(this, "factory is not exists\n\tstatus: $status")
  }
  
  fun getUnit(): Any {
    if (status != UnitStatus.INITIALIZED)
      throw RuntimeUnitException(this, "unit is not valid status\n\tstatus: $status")
    return factory ?: throw RuntimeUnitException(this, "factory is not exists\n\tstatus: $status")
  }
  
  @Synchronized
  fun initialized(unit: Any): MutableList<Throwable> {
    status = UnitStatus.INITIALIZED
    this.unit = unit
    val errors = mutableListOf<Throwable>()
    initializedLock.signeAll()
    return errors
  }
  
  fun initializedBlock(time: Long, timeUnit: TimeUnit) {
    initializedLock.block(time, timeUnit)
  }
  
  @Synchronized
  fun initializing() {
    status = UnitStatus.INITIALIZED
  }
  
  @Synchronized
  fun loaded(factory: UnitFactory) {
    status = UnitStatus.LOADED
    this.factory = factory
  }
  
  @Synchronized
  fun fail() {
    status = UnitStatus.FAIL
    initializedLock.signeAll()
  }
}