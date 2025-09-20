package io.github.pandier.snowball.impl.registry

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.registry.RegistryReference
import io.github.pandier.snowball.registry.SnowballRegistries
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.key.Key
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

class SnowballRegistriesImpl : SnowballRegistries {
    val itemComponentTypes = SnowballItemComponentTypeRegistry().registerDefaults()

    @Suppress("UNCHECKED_CAST")
    override fun <T> itemComponentType(key: Key): RegistryReference<ItemComponentType<T>> {
        val itemComponentType = (itemComponentTypes.get(key) ?: throw IllegalArgumentException("Unknown entry '$key' for item component type registry")) as ItemComponentType<T>
        return RegistryReference { itemComponentType }
    }

    override fun itemType(key: Key): RegistryReference<ItemType> {
        return ref(Registries.ITEM, key) { Conversions.snowball(it) }
    }

    override fun blockType(key: Key): RegistryReference<BlockType> {
        return ref(Registries.BLOCK, key) { Conversions.snowball(it) }
    }

    private fun <T, V> ref(registry: Registry<T>, key: Key, transformer: (T) -> V): RegistryReference<V> {
        val entry = registry.getEntry(Conversions.Adventure.vanilla(key))
            .orElseThrow { IllegalArgumentException("Unknown entry '$key' for registry '${registry.key}'") }
        return RegistryReference { transformer(entry.value()) }
    }
}