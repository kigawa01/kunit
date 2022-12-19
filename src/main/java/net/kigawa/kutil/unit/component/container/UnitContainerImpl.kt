package net.kigawa.kutil.unit.component.container

import net.kigawa.kutil.unit.*
import net.kigawa.kutil.unit.component.*
import net.kigawa.kutil.unit.component.database.*
import net.kigawa.kutil.unit.container.*
import net.kigawa.kutil.unit.exception.*
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import java.util.*
import java.util.concurrent.*

class UnitContainerImpl(
  private val parent: UnitContainer? = null,
  vararg units: Any,
): UnitContainer {
  private val database = UnitInfoDatabaseComponentImpl()
  override fun removeUnit(identify: UnitIdentify<Any>) {
    database.
  }
  
  override fun getIdentifies(): MutableList<UnitIdentify> {
    val list = mutableListOf<UnitIdentify>()
    list.addAll(unitDatabase.identifyList())
    parent?.let {list.addAll(it.getIdentifies())}
    return list
  }
  
  override fun <T> getUnitList(identify: UnitIdentify<T>): List<T> {
    TODO("Not yet implemented")
  }
  
  override fun close() {
    removeUnit(Any::class.java).forEach {it.printStackTrace()}
  }
  
  @Suppress("UNCHECKED_CAST")
  override fun <T> getUnitList(unitClass: Class<T>, name: String?): List<T> {
    val identify = UnitIdentify(unitClass, name)
    val units = unitDatabase.findInfo(identify).map {
      it.getUnit() as T
    }.toMutableList()
    parent?.getUnitList(unitClass)?.let {units.addAll(it)}
    
    return units
  }
  
  override fun <T> contain(identify: UnitIdentify<T>): Boolean {
    TODO("Not yet implemented")
  }
}