package io.github.pandier.snowball.impl.registry

import com.google.common.base.Suppliers
import io.github.pandier.snowball.entity.AttributeType
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.EntityType
import io.github.pandier.snowball.entity.damage.DamageType
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.SnowballImpl
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.registry.RegistryReference
import io.github.pandier.snowball.registry.SnowballRegistries
import io.github.pandier.snowball.scoreboard.Criterion
import io.github.pandier.snowball.world.GameRule
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.key.Key
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.scores.criteria.ObjectiveCriteria
import java.util.function.Supplier

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
    override fun <T : Entity> entityType(key: Key): RegistryReference<EntityType<T>> {
        // TODO: Type safety
        return RegistryReferenceImpl.Lazy(Registries.ENTITY_TYPE, key) { Conversions.snowball(it) as EntityType<T> }
    }

    override fun <T : Entity> entityType(entry: EntityType<T>): RegistryReference<EntityType<T>> {
        return fromEntry(Registries.ENTITY_TYPE, entry)
    }

    override fun attributeType(key: Key): RegistryReference<AttributeType> {
        return RegistryReferenceImpl.Lazy(Registries.ATTRIBUTE, key, Conversions::snowball)
    }

    override fun attributeType(entry: AttributeType): RegistryReference<AttributeType> {
        return fromEntry(Registries.ATTRIBUTE, entry)
    }

    override fun criterion(name: String): Supplier<Criterion> {
        return Suppliers.memoize {
            ObjectiveCriteria.byName(name)
                .orElseThrow { IllegalArgumentException("Unknown criterion '$name'") }
                .let(Conversions::snowball)
        }
    }

    override fun gameRuleAny(key: Key): RegistryReference<GameRule<*>> {
        return RegistryReferenceImpl.Lazy(Registries.GAME_RULE, key, Conversions::snowballAny)
    }

    override fun gameRuleAny(entry: GameRule<*>): RegistryReference<GameRule<*>> {
        return fromEntry(Registries.GAME_RULE, entry)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> gameRule(key: Key, type: Class<T>): RegistryReference<GameRule<T>> {
        return RegistryReferenceImpl.Lazy(Registries.GAME_RULE, key) {
            if (it.valueClass() != type)
                error("Type ${type.name} for game rule '$key' doesn't match type in registry (${it.valueClass().name})")
            Conversions.snowball(it as net.minecraft.world.level.gamerules.GameRule<T>)
        }
    }

    override fun <T : Any> gameRule(entry: GameRule<T>): RegistryReference<GameRule<T>> {
        return fromEntry(Registries.GAME_RULE, entry)
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