package net.kigawa.kutil.unitapi.annotation

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Kunit(
    val name: String = "",
)