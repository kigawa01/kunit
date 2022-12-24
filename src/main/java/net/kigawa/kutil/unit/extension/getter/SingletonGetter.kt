package net.kigawa.kutil.unit.extension.getter

import net.kigawa.kutil.unit.component.config.UnitContainerConfig
import net.kigawa.kutil.unit.component.async.AsyncComponent
import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.component.factory.UnitFactoryComponent
import net.kigawa.kutil.unit.concurrent.ThreadLock
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class SingletonGetter(
  private val factoryComponent: UnitFactoryComponent,
  private val async: AsyncComponent,
  private val components: UnitContainerConfig,
): UnitGetter {
  private var obj: Any? = null
  private var registered = false
  private var initLock: ThreadLock? = null
  
  override fun <T: Any> get(identify: UnitIdentify<T>): T {
    @Suppress("UNCHECKED_CAST")
    return obj as T? ?: throw UnitException("unit is not initialized", identify)
  }
  
  override fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): Future<T> {
    return async.submit(identify) {
      initGetter(identify, initStack)
      get(identify)
    }
  }
  
  override fun initGetter(identify: UnitIdentify<out Any>, initStack: InitStack) {
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
  
  override fun register(identify: UnitIdentify<out Any>, options: RegisterOptions): Boolean {
    if (obj != null) return false
    if (registered) return false
    registered = true
    return true
  }
}