package net.kigawa.kutil.kunit.exception

import net.kigawa.kutil.kunit.provider.Provider

class CircularItemException(clazz: Provider<out Any>) : RuntimeException() {}