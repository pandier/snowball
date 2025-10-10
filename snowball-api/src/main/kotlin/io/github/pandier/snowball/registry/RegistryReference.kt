package io.github.pandier.snowball.registry

import net.kyori.adventure.key.Key
import java.util.function.Supplier

public interface RegistryReference<out T> : Supplier<@UnsafeVariance T> {
    public val key: Key
}
