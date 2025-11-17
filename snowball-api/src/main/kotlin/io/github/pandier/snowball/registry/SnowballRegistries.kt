package io.github.pandier.snowball.registry

import io.github.pandier.snowball.entity.AttributeType
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.EntityType
import io.github.pandier.snowball.entity.damage.DamageType
import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.scoreboard.Criterion
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.key.Key
import java.util.function.Supplier

public interface SnowballRegistries {
    public fun itemType(key: Key): RegistryReference<ItemType>
    public fun itemType(entry: ItemType): RegistryReference<ItemType>

    public fun blockType(key: Key): RegistryReference<BlockType>
    public fun blockType(entry: BlockType): RegistryReference<BlockType>

    public fun itemComponentType(key: Key): ItemComponentType<*>
    public fun <T> itemComponentType(key: Key, type: Class<T>): ItemComponentType<T>

    public fun damageType(key: Key): RegistryReference<DamageType>
    public fun damageType(entry: DamageType): RegistryReference<DamageType>

    public fun <T : Entity> entityType(entry: EntityType<T>): RegistryReference<EntityType<T>>
    public fun <T : Entity> entityType(key: Key): RegistryReference<EntityType<T>>

    public fun attributeType(key: Key): RegistryReference<AttributeType>
    public fun attributeType(entry: AttributeType): RegistryReference<AttributeType>

    public fun criterion(name: String): Supplier<Criterion>
}
