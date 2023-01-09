package net.kigawa.kutil.unit.util

object Util {
  fun createStringList(items: Iterable<Any?>): List<String> {
    val list = mutableListOf<Any>()
    items.forEach {
      it?.let {list.add(it)}
    }
    val result = mutableListOf<String>()
    
    list.forEach {item->
      if (item is Iterable<*>) {
        result.add(">${item.javaClass.simpleName}")
        result.addAll(createStringList(item).map {"  $it"})
      } else result.add(">$item")
    }
    return result
  }
  
  fun createStringList(message: String, items: Iterable<Any?>): List<String> {
    val list = createStringList(items).toMutableList()
    list.add(0, message)
    return list
  }
  
  fun createMessage(message: String, item: Iterable<Any?>): String {
    return createStringList(message, item).joinToString("\n ")
  }
}

@Suppress("unused")
class Message private constructor(val message: String, private val child: Iterable<Message>) {
  override fun toString(): String {
    return "$message> $child"
  }
}