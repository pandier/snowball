package io.github.pandier.snowball.registry

import java.util.function.Supplier

public fun interface RegistryReference<out T> : Supplier<@UnsafeVariance T>
