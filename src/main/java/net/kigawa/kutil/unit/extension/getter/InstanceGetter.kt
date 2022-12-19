package net.kigawa.kutil.unit.extension.getter

import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.*
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

class InstanceGetter(): UnitGetter {
  private var obj: Any? = null
  
  override fun <T> get(identify: UnitIdentify<T>): T {
    @Suppress("UNCHECKED_CAST")
    return obj as T? ?: throw UnitException("unit is not initialized", identify)
  }
  
  override fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): Future<T> {
    val future = FutureTask {get(identify)}
    future.run()
    return future
  }
  
  override fun init(identify: UnitIdentify<out Any>, initStack: InitStack) {
  }
  
  @Synchronized
  override fun register(identify: UnitIdentify<out Any>, options: RegisterOptions): Boolean {
    if (obj != null) return false
    val instanceOption = options.find(InstanceOption::class.java) ?: return false
    obj = instanceOption.instance
    return true
  }
}