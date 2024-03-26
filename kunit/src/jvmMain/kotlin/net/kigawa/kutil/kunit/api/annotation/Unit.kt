package net.kigawa.kutil.kunit.api.annotation

@Deprecated("use @Kunit")
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Unit(
  val name: String = "",
)