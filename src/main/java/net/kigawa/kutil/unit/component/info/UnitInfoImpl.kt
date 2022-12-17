package net.kigawa.kutil.unit.component.info

import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.component.UnitStatus
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.factory.UnitFactory
import net.kigawa.kutil.unit.extension.getter.UnitGetter
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import java.util.concurrent.TimeUnit

class UnitInfoImpl<T>(
  identify: UnitIdentify<T>,
): UnitInfo<T> {
  override val identify: UnitIdentify<T>
  override var fail: Boolean
    get() = TODO("Not yet implemented")
    set(value) {}
  var getter: UnitGetter
  var factoryClass: Class<out UnitFactory>
  
  init {
    val name = if (identify.name == null || identify.name == "") {
      val unitAnnotation = identify.unitClass.getAnnotation(Unit::class.java)
      if (unitAnnotation == null || unitAnnotation.name == "") identify.unitClass.name
      else unitAnnotation.name
    } else identify.name
    
    this.identify = UnitIdentify(identify.unitClass, name)
  }
  
  var status: UnitStatus = UnitStatus.NOT_LOADED
    private set
  private var unit: Any? = null
  private var factory: UnitFactory? = null
  
  fun getFactory(): UnitFactory {
    if (status == UnitStatus.NOT_LOADED) throw UnitException(this, "unit is not valid status\n\tstatus: $status")
    return factory ?: throw UnitException(this, "factory is not exists\n\tstatus: $status")
  }
  
  fun getUnit(): Any {
    if (status != UnitStatus.INITIALIZED)
      throw UnitException(this, "unit is not valid status\n\tstatus: $status")
    return factory ?: throw UnitException(this, "factory is not exists\n\tstatus: $status")
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