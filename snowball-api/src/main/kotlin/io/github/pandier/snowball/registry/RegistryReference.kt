package io.github.pandier.snowball.registry

import java.util.function.Supplier

public fun interface RegistryReference<T> : Supplier<T>
