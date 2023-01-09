package net.kigawa.kutil.unit.annotation

@Deprecated("use @Kunit")
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Unit(
  val name: String = "",
)