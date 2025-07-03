package io.github.pandier.snowball.impl.registry

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.registry.RegistryReference
import io.github.pandier.snowball.registry.SnowballRegistries
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.key.Key
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object SnowballRegistriesImpl : SnowballRegistries {

    override fun blockType(key: Key): RegistryReference<BlockType> {
        return ref(Registries.BLOCK, key) { Conversions.snowball(it) }
    }

    private fun <T, V> ref(registry: Registry<T>, key: Key, transformer: (T) -> V): RegistryReference<V> {
        val entry = registry.getEntry(Conversions.Adventure.vanilla(key))
            .orElseThrow { IllegalArgumentException("Unknown entry '$key' for registry '${registry.key}'") }
        return RegistryReference { transformer(entry.value()) }
    }
}