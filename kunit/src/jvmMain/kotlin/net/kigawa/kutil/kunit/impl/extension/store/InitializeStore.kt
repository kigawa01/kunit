package net.kigawa.kutil.kunit.impl.extension.store

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.*
import net.kigawa.kutil.kunit.api.extention.UnitStore
import net.kigawa.kutil.kunit.api.options.RegisterOptionEnum
import net.kigawa.kutil.kunit.api.options.RegisterOptions

@LateInit
class InitializeStore(
  private val factoryComponent: UnitFactoryComponent,
): UnitStore {
  override fun <T: Any> get(identify: UnitIdentify<T>): T {
    return factoryComponent.init(identify, InitStack())
  }
  
  override fun <T: Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): T {
    return factoryComponent.init(identify, initStack)
  }
  
  override fun initGetter(identify: UnitIdentify<out Any>, initStack: InitStack) {
  }
  
  override fun register(identify: UnitIdentify<out Any>, options: RegisterOptions): Boolean {
    return options.contain(RegisterOptionEnum.ALWAYS_INIT)
  }
}