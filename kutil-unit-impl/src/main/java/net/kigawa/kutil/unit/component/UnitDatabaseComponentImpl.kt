@file:Suppress("unused")

package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.*
import net.kigawa.kutil.unit.util.LocaleBuilder
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.*
import net.kigawa.kutil.unitapi.options.RegisterOptions
import java.util.*
import java.util.logging.Level

@LateInit
class UnitDatabaseComponentImpl(
  private val componentDatabase: ComponentDatabase,
): UnitDatabaseComponent {
  internal lateinit var loggerComponent: UnitLoggerComponent
  private val databases = ConcurrentList<UnitInfoDatabase>(componentDatabase)
  
  init {
    addDatabase(DefaultInfoDatabase())
  }
  
  fun setLoggerComponent(loggerComponent: UnitLoggerComponent) {
    this.loggerComponent = loggerComponent
  }
  
  override fun getComponentDatabase(): ComponentDatabase {
    return componentDatabase
  }
  
  override fun addDatabase(unitInfoDatabase: UnitInfoDatabase) {
    databases.add(unitInfoDatabase)
    componentDatabase.registerComponent(unitInfoDatabase, null)
  }
  
  override fun removeDatabase(unitInfoDatabase: UnitInfoDatabase) {
    componentDatabase.unregisterComponent(unitInfoDatabase.javaClass)
    databases.remove(unitInfoDatabase)
  }
  
  override fun registerInfo(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions) {
    databases.last {
      try {
        it.register(unitInfo, registerOptions)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder("there an exception when register info").toString(),
            listOf(e),
            listOf(unitInfo, registerOptions, it)
          )
        )
        false
      }
    }
  }
  
  override fun unregisterInfo(unitInfo: UnitInfo<*>) {
    databases.forEach {
      try {
        it.unregister(unitInfo)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder("there an exception when unregister info").toString(),
            listOf(e),
            listOf(unitInfo, it)
          )
        )
      }
    }
  }
  
  override fun <T: Any> findByIdentify(identify: UnitIdentify<T>): List<UnitInfo<T>> {
    return databases.flatMap {
      try {
        it.findByIdentify(identify)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder("there an exception when find info").toString(),
            listOf(e),
            listOf(identify, it)
          )
        )
        listOf()
      }
    }
  }
}