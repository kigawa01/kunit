package net.kigawa.kutil.kunit.impl.extension.store

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.AlwaysInit
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.component.UnitConfigComponent
import net.kigawa.kutil.kunit.api.component.UnitFactoryComponent
import net.kigawa.kutil.kunit.api.exception.UnitException
import net.kigawa.kutil.kunit.api.extention.UnitStore
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.impl.concurrent.ThreadLock
import java.util.concurrent.TimeUnit

@AlwaysInit
class SingletonStore(
  private val factoryComponent: UnitFactoryComponent,
  private val components: UnitConfigComponent,
) : UnitStore {
  private var obj: Any? = null
  private var registered = false
  private var initLock: ThreadLock? = null

  override fun <T : Any> get(identify: UnitIdentify<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return obj as T?
  }

  override fun <T : Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): T {
    initGetter(identify, initStack)
    return get(identify) ?: throw UnitException("unit is not initialized", identify = identify)
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