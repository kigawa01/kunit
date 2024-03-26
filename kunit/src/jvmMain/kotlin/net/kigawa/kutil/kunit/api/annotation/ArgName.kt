package net.kigawa.kutil.kunit.api.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ArgName(val name: String)
