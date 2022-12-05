package net.kigawa.kutil.unit.extension.factory

import net.kigawa.kutil.unit.UnitIdentify

abstract class UnitFactory {
  abstract fun isValid(unitIdentify: UnitIdentify<*>): Boolean
  abstract fun init(unitIdentify: UnitIdentify<*>): Any
  abstract fun configureDependencies(unitIdentify: UnitIdentify<*>, get: DependencyGetter)
}

interface DependencyGetter {
  fun <T> get(unitIdentify: UnitIdentify<T>): T?
}