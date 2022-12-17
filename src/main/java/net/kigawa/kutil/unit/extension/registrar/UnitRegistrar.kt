package net.kigawa.kutil.unit.extension.registrar

interface UnitRegistrar {
  fun register(registrarInfo: UnitRegistrarInfo<*>): Boolean
  fun unregister(registrarInfo: UnitRegistrarInfo<*>): Boolean
}