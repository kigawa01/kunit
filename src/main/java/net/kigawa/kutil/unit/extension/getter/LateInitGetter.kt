package net.kigawa.kutil.unit.extension.getter

import net.kigawa.kutil.unit.annotation.getter.AlwaysInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.api.component.UnitAsyncComponent
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.api.component.UnitFactoryComponent
import net.kigawa.kutil.unit.extension.registeroption.DefaultRegisterOption
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions
import java.util.concurrent.Future

@AlwaysInit
class LateInitGetter(
  private val factoryComponent: UnitFactoryComponent,
  private val async: UnitAsyncComponent,
): UnitGetter {
  private var obj: Any? = null
  private var registered = false
  
  override fun <T: Any> get(identify: UnitIdentify<T>): T {
    synchronized(this) {
      @Suppress("UNCHECKED_CAST")
      if (obj != null) return obj as T
      val obj = factoryComponent.init(identify, InitStack())
      this.obj = obj
      return obj
    }
  }
  
  override fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): Future<T> {
    return async.submit(identify) {
      get(identify)
    }
  }
  
  override fun initGetter(identify: UnitIdentify<out Any>, initStack: InitStack) {
  }
  
  override fun register(identify: UnitIdentify<out Any>, options: RegisterOptions): Boolean {
    if (obj != null) return false
    if (registered) return false
    registered = true
    return options.match(DefaultRegisterOption.LATE_INIT, identify.unitClass)
  }
}