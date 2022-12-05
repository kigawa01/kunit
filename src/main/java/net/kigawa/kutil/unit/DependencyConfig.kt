package net.kigawa.kutil.unit

class DependencyConfig {
  fun <T> add(dependencyClass: UnitIdentify<T>, setter: (T)->Unit) {
  }
  
  fun <T> add(dependencyClass: Iterable<UnitIdentify<T>>, setter: (Iterable<T>)->Unit) {
  }
}