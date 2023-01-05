package net.kigawa.kutil.unit.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedArg(val name: String)
