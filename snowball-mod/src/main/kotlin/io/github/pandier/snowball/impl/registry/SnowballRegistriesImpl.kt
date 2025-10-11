package io.github.pandier.snowball.impl.registry

import io.github.pandier.snowball.entity.damage.DamageType
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.SnowballImpl
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.registry.RegistryReference
import io.github.pandier.snowball.registry.SnowballRegistries
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.key.Key
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey

class SnowballRegistriesImpl : SnowballRegistries {
    private val itemComponentTypes = SnowballItemComponentTypeRegistry().registerDefaults()

    override fun itemType(key: Key): RegistryReference<ItemType> {
        return RegistryReferenceImpl.Lazy(Registries.ITEM, key, Conversions::snowball)
    }

    override fun itemType(entry: ItemType): RegistryReference<ItemType> {
        return fromEntry(Registries.ITEM, entry)
    }

    override fun blockType(key: Key): RegistryReference<BlockType> {
        return RegistryReferenceImpl.Lazy(Registries.BLOCK, key, Conversions::snowball)
    }

    override fun blockType(entry: BlockType): RegistryReference<BlockType> {
        return fromEntry(Registries.BLOCK, entry)
    }

    override fun itemComponentType(key: Key): ItemComponentType<*> {
        return itemComponentTypes.get(key) ?: throw IllegalArgumentException("Unknown entry '$key' for item component type registry")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> itemComponentType(key: Key, type: Class<T>): ItemComponentType<T> {
        val entry = itemComponentTypes.get(key) ?: throw IllegalArgumentException("Unknown entry '$key' for item component type registry")
        if (entry.type == null || !type.isAssignableFrom(entry.type))
            throw IllegalArgumentException("Type ${type.name} for item component type '$key' doesn't match type in registry (${entry.type?.name})")
        return entry as ItemComponentType<T>
    }

    fun itemComponentType(vanilla: DataComponentType<*>): ItemComponentType<*> {
        return itemComponentTypes.get(vanilla)
    }

    override fun damageType(key: Key): RegistryReference<DamageType> {
        return RegistryReferenceImpl.Lazy(Registries.DAMAGE_TYPE, key, Conversions::snowball)
    }

    override fun damageType(entry: DamageType): RegistryReference<DamageType> {
        return fromEntry(Registries.DAMAGE_TYPE, entry)
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <T : Any, V : Any> fromEntry(
        registryKey: ResourceKey<Registry<V>>,
        entry: T,
        transformer: (T) -> V = { (it as SnowballAdapter).adaptee as V }
    ): RegistryReference<T> {
        val registry = SnowballImpl.server.adaptee.registryAccess().lookupOrThrow(registryKey)
        val key = (registry.getKey(transformer(entry)) ?: error("Unknown entry for registry '$registryKey': $entry"))
            .let(Conversions.Adventure::adventure)
        return RegistryReferenceImpl.Direct(registryKey, key, entry)
    }
}