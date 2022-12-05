package net.kigawa.kutil.unit.componate.initializer

import net.kigawa.kutil.unit.*
import net.kigawa.kutil.unit.exception.*
import java.util.concurrent.*

class UnitInitializer {
  fun <T> initUnitsAsync(unitClass: Class<T>, name: String?): FutureTask<MutableList<Throwable>> {
    val unitIdentify = UnitIdentify(unitClass, name)
    val future = FutureTask {
      val errors = mutableListOf<Throwable>()
      unitDatabase.findInfo(unitIdentify).map {
        val future = FutureTask {
          try {
            initTask(it)
          } catch (e: Throwable) {
            errors.add(e)
            it.fail()
            null
          }
        }
        executor {future.run()}
        return@map future
      }.forEach {
        try {
          it.get(timeoutSec + 1, TimeUnit.SECONDS)
        } catch (e: TimeoutException) {
          errors.add(RuntimeUnitException(unitClass, name, "could not init unit", e))
        } catch (e: ExecutionException) {
          errors.add(RuntimeUnitException(unitClass, name, "could not init unit", e.cause))
        }
      }
      return@FutureTask errors
    }
    executor.run {future.run()}
    return future
  }
  private fun initTask(info: UnitInfo): MutableList<Throwable> {
    val errors = mutableListOf<Throwable>()
    synchronized(info) {
      if (info.status == UnitStatus.INITIALIZED || info.status == UnitStatus.INITIALIZING || info.status == UnitStatus.FAIL)
        return errors
      if (info.status != UnitStatus.LOADED) {
        errors.add(RuntimeUnitException(info, "could not init unit\n\tstatus: ${info.status}"))
        return errors
      }
      info.initializing()
    }
    val factory = info.getFactory()
    val dependencyInfo = factory.configureDependencies(info.identify).map {
      val dependencyInfoList = unitDatabase.findInfoAmbiguous(it)
      if (dependencyInfoList.isEmpty()) {
        errors.add(NoFoundUnitException(info, "unit dependency not found"))
        return errors
      }
      if (dependencyInfoList.size != 1) {
        errors.add(NoSingleUnitException(info, "unit dependency found no single"))
        return errors
      }
      return@map dependencyInfoList[0]
    }
    dependencyInfo.map {
      if (it.status == UnitStatus.LOADED) initUnitsAsync(it.identify.unitClass, it.identify.name)
      else null
    }.forEach {
      try {
        it?.let {errors.addAll(it.get())}
      } catch (e: Throwable) {
        errors.add(e)
        return errors
      }
    }
    
    val dependencies = dependencyInfo.map {
      synchronized(it) {
        if (it.status == UnitStatus.INITIALIZING) it.initializedBlock(timeoutSec, TimeUnit.SECONDS)
        return@map it.getUnit()
      }
    }
    info.initialized(factory.init(info.identify, dependencies))
    return errors
  }
  
}