package net.kigawa.kutil.unit.extension.getter

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.api.component.UnitAsyncComponent
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.api.component.UnitFactoryComponent
import net.kigawa.kutil.unit.extension.registeroption.DefaultRegisterOption
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions
import java.util.concurrent.Future

@LateInit
class InitializeGetter(
  private val factoryComponent: UnitFactoryComponent,
  private val asyncComponent: UnitAsyncComponent,
): UnitGetter {
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
    return options.match(DefaultRegisterOption.ALWAYS_INIT, identify.unitClass)
  }
}