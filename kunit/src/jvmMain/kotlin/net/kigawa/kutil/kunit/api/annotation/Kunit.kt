package net.kigawa.kutil.kunit.api.annotation

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Kunit(
    val name: String = "",
)