package io.github.pandier.snowball.impl.registry

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.SnowballImpl
import io.github.pandier.snowball.registry.RegistryReference
import net.kyori.adventure.key.Key
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

sealed class RegistryReferenceImpl<out T, out V>(
    protected val registryKey: ResourceKey<out Registry<out V>>,
    override val key: Key,
) : RegistryReference<T> {
    class Lazy<out T, out V>(
        registryKey: ResourceKey<out Registry<out V>>,
        key: Key,
        transformer: (V) -> T,
    ) : RegistryReferenceImpl<T, V>(registryKey, key) {
        private val value = lazy {
            SnowballImpl.server.adaptee.registryAccess()
                .lookupOrThrow(registryKey)
                .get(Conversions.Adventure.vanilla(key))
                .orElseThrow { IllegalArgumentException("Unknown entry '$key' for registry '$registryKey'") }
                .value()
                .let(transformer)
        }

        override fun get(): T = value.value
    }

    class Direct<out T, out V>(
        registryKey: ResourceKey<out Registry<out V>>,
        key: Key,
        private val value: T
    ) : RegistryReferenceImpl<T, V>(registryKey, key) {
        override fun get(): T = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as RegistryReferenceImpl<*, *>
        return registryKey == other.registryKey && key == other.key
    }

    override fun hashCode(): Int {
        return 31 * registryKey.hashCode() + key.hashCode()
    }
}