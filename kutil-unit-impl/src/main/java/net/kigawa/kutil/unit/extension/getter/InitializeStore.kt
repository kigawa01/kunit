package net.kigawa.kutil.unit.extension.getter

import net.kigawa.kutil.unit.extension.registeroption.DefaultRegisterOption
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.extention.UnitStore
import java.util.concurrent.Future

@LateInit
class InitializeStore(
  private val factoryComponent: UnitFactoryComponent,
  private val asyncComponent: UnitAsyncComponent,
): UnitStore {
  override fun <T: Any> get(identify: UnitIdentify<T>): T {
    return factoryComponent.init(identify, InitStack())
  }
  
  override fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): Future<T> {
    return asyncComponent.submit(identify) {
      factoryComponent.init(identify, initStack)
    }
  }
  
  override fun initGetter(identify: UnitIdentify<out Any>, initStack: InitStack) {
  }
  
  override fun register(identify: UnitIdentify<out Any>, options: RegisterOptions): Boolean {
    return options.contain(DefaultRegisterOption.ALWAYS_INIT)
  }
}