package net.kigawa.kutil.unit.extension.getter

import net.kigawa.kutil.unit.extension.registeroption.DefaultRegisterOption
import net.kigawa.kutil.unitapi.annotation.getter.AlwaysInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.RegisterOptions
import net.kigawa.kutil.unitapi.extention.UnitStore
import java.util.concurrent.Future

@Suppress("unused")
@AlwaysInit
class LateInitStore(
  private val factoryComponent: UnitFactoryComponent,
  private val async: UnitAsyncComponent,
): UnitStore {
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
    return options.contain(DefaultRegisterOption.LATE_INIT)
  }
}