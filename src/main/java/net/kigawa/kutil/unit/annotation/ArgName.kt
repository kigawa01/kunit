package net.kigawa.kutil.unit.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ArgName(val name: String)
