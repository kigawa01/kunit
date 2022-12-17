package net.kigawa.kutil.unit.extension.registrar

interface UnitRegistrar {
  fun register(registrarInfo: UnitRegistrarInfo, errors: MutableList<Throwable>): Boolean
  fun unregister(registrarInfo: UnitRegistrarInfo, errors: MutableList<Throwable>): Boolean
}