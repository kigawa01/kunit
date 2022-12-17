package net.kigawa.kutil.unit.extension.registrarinfo

import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import java.io.File
import java.io.IOException
import java.net.JarURLConnection
import java.net.URL
import java.util.*

class JarRegistrarInfo(resource: URL, packageName: String): AbstractRegistrarInfo() {
  companion object {
    const val PROTOCOL = "jar"
  }
  
  constructor(resource: URL, packageName: Package): this(resource, packageName.name)
  
  @Suppress("unused")
  constructor(jarFile: File): this(jarFile.toURI().toURL(), "")
  
  init {
    if (PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")
    
    try {
      (resource.openConnection() as JarURLConnection).jarFile.use {jarFile->
        for (entry in Collections.list(jarFile.entries())) {
          var name = entry.name
          if (!name.startsWith(packageName.replace('.', '/'))) continue
          if (!name.endsWith(".class")) continue
          name = name.replace('/', '.').replace(".class$".toRegex(), "")
          try {
            identifies.add(UnitIdentify(Class.forName(name), null))
          } catch (e: Throwable) {
            errors.add(UnitException("could not load unit: $name", e))
          }
        }
      }
    } catch (e: IOException) {
      throw RuntimeException("could not load units file", e)
    }
  }
}