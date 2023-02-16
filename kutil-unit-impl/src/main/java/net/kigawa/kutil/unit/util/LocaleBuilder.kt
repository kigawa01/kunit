package net.kigawa.kutil.unit.util

import java.util.*

class LocaleBuilder(private var defaultLocale: Locale, message: String) {
  private val messages = mutableMapOf<String, String>()
  
  constructor(message: String): this(Locale.ENGLISH, message)
  
  init {
    messages[defaultLocale.language] = message
  }
  
  @Suppress("unused")
  fun on(locale: Locale, message: String) {
    messages[locale.language] = message
  }
  
  @Suppress("unused")
  fun getMessages(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    map.putAll(messages)
    return map
  }
  
  override fun toString(): String {
    return messages[Locale.getDefault().language] ?: messages[defaultLocale.language] ?: "message not found"
  }
}