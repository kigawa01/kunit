package net.kigawa.kutil.kunit.api.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Dependencies
  (vararg val value: Dependency = [])
