package net.kigawa.kutil.unitapi.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ArgName(val name: String)
