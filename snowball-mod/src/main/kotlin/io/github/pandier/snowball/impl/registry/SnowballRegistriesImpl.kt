package io.github.pandier.snowball.impl.registry

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.registry.SnowballRegistries
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.key.Key
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries

class SnowballRegistriesImpl : SnowballRegistries {
    private val itemComponentTypes = SnowballItemComponentTypeRegistry().registerDefaults()

    @Suppress("UNCHECKED_CAST")
    override fun <T> itemComponentType(key: Key): ItemComponentType<T> {
        return (itemComponentTypes.get(key) ?: throw IllegalArgumentException("Unknown entry '$key' for item component type registry")) as ItemComponentType<T>
    }

    fun itemComponentType(vanilla: DataComponentType<*>): ItemComponentType<*> {
        return itemComponentTypes.get(vanilla)
    }

    override fun itemType(key: Key): ItemType {
        return ref(BuiltInRegistries.ITEM, key) { Conversions.snowball(it) }
    }

    override fun blockType(key: Key): BlockType {
        return ref(BuiltInRegistries.BLOCK, key) { Conversions.snowball(it) }
    }

    private fun <T, V> ref(registry: Registry<T>, key: Key, transformer: (T) -> V): V {
        val entry = registry.get(Conversions.Adventure.vanilla(key))
            .orElseThrow { IllegalArgumentException("Unknown entry '$key' for registry '${registry.key()}'") }
        return transformer(entry.value())
    }
}