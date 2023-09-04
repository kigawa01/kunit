package net.kigawa.kutil.unitimpl.extension.store

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.AlwaysInit
import net.kigawa.kutil.unitapi.component.InitStack
import net.kigawa.kutil.unitapi.exception.UnitStoreException
import net.kigawa.kutil.unitapi.extention.UnitStore
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.options.RegistrarInstanceOption

@AlwaysInit
class InstanceStore : UnitStore {
  private var obj: Any? = null

  override fun <T : Any> get(identify: UnitIdentify<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return obj as T?
  }

  override fun <T : Any> initOrGet(identify: UnitIdentify<T>, initStack: InitStack): T {
    return get(identify) ?: throw UnitStoreException("instance store unit is must not be null", identify)
  }

  override fun initGetter(identify: UnitIdentify<out Any>, initStack: InitStack) {
  }

  @Synchronized
  override fun register(identify: UnitIdentify<out Any>, options: RegisterOptions): Boolean {
    if (obj != null) return false
    val registrarInstanceOption = options.firstOrNull(RegistrarInstanceOption::class.java) ?: return false
    obj = registrarInstanceOption.instance
    return true
  }
}