package net.kigawa.kutil.unit.extension.getter

import net.kigawa.kutil.unit.component.UnitContainerConponents
import net.kigawa.kutil.unit.component.async.Async
import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponent
import net.kigawa.kutil.unit.concurrent.ThreadLock
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOption
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class SingletonGetter(
  private val factoryComponent: UnitFactoryComponent,
  private val async: Async,
  private val components: UnitContainerConponents,
): UnitGetter {
  private var obj: Any? = null
  private var registered = false
  private var initLock: ThreadLock? = null
  
  override fun <T> get(identify: UnitIdentify<T>): T {
    @Suppress("UNCHECKED_CAST")
    return obj as T? ?: throw UnitException("unit is not initialized", identify)
  }
  
  override fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): Future<T> {
    return async.submit(identify){
      init(identify,initStack)
      get(identify)
    }
  }
  
  override fun init(identify: UnitIdentify<out Any>, initStack: InitStack) {
    initLock?.block(components.timeoutSec, TimeUnit.SECONDS)
    synchronized(this) {
      if (obj != null) return
      initLock = ThreadLock()
    }
    val obj = factoryComponent.init(identify, initStack)
    synchronized(this) {
      this.obj = obj
      initLock?.signeAll()
      initLock = null
    }
  }
  
  @Synchronized
  override fun register(identify: UnitIdentify<out Any>, options: List<RegisterOption>): Boolean {
    if (obj != null) return true
    if (registered) return true
    registered = true
    return true
  }
}