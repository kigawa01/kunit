package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.extension.registrarinfo.UnitRegistrarInfo

interface UnitRegistrar {
  fun register(registrarInfo: UnitRegistrarInfo<*>): Boolean
  fun unregister(registrarInfo: UnitRegistrarInfo<*>): Boolean
}