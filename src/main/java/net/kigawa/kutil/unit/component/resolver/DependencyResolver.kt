package net.kigawa.kutil.unit.component.resolver

import net.kigawa.kutil.unit.component.container.UnitIdentify

class DependencyResolver {
  fun <T> add(dependencyClass: UnitIdentify<T>, setter: (T)->Unit) {
  }
  
  fun <T> add(dependencyClass: Iterable<UnitIdentify<T>>, setter: (Iterable<T>)->Unit) {
  }
}