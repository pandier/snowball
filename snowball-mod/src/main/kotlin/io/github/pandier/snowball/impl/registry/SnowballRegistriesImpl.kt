package io.github.pandier.snowball.impl.registry

import io.github.pandier.snowball.entity.damage.DamageType
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.SnowballImpl
import io.github.pandier.snowball.impl.entity.damage.DamageTypeImpl
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

    override fun itemType(key: Key): ItemType {
        return RegistryReferenceImpl.Lazy(Registries.ITEM, key, Conversions::snowball).get()
    }

    override fun blockType(key: Key): BlockType {
        return RegistryReferenceImpl.Lazy(Registries.BLOCK, key, Conversions::snowball).get()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> itemComponentType(key: Key): ItemComponentType<T> {
        return (itemComponentTypes.get(key) ?: throw IllegalArgumentException("Unknown entry '$key' for item component type registry")) as ItemComponentType<T>
    }

    fun itemComponentType(vanilla: DataComponentType<*>): ItemComponentType<*> {
        return itemComponentTypes.get(vanilla)
    }

    override fun damageType(entry: DamageType): RegistryReference<DamageType> {
        return fromEntry(Registries.DAMAGE_TYPE, entry) { (it as DamageTypeImpl).adaptee }
    }

    override fun damageType(key: Key): RegistryReference<DamageType> {
        return RegistryReferenceImpl.Lazy(Registries.DAMAGE_TYPE, key, Conversions::snowball)
    }

    private fun <T : Any, V : Any> fromEntry(registryKey: ResourceKey<Registry<V>>, entry: T, transformer: (T) -> V): RegistryReference<T> {
        val registry = SnowballImpl.server.adaptee.registryAccess().lookupOrThrow(registryKey)
        val key = (registry.getKey(transformer(entry)) ?: error("Unknown entry for registry '$registryKey': $entry"))
            .let(Conversions.Adventure::adventure)
        return RegistryReferenceImpl.Direct(registryKey, key, entry)
    }
}