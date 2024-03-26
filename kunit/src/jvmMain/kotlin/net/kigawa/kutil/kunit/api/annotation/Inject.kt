package net.kigawa.kutil.kunit.api.annotation

@Target(
  AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER
)
@Retention(
  AnnotationRetention.RUNTIME
)
annotation class Inject
