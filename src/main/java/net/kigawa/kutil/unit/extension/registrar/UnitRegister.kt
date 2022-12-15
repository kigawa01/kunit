package net.kigawa.kutil.unit.extension.registrar

interface UnitRegister {
  fun register(registrarInfo: UnitRegistrarInfo): Boolean
}