package io.github.pandier.snowball.registry

import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.key.Key

public interface SnowballRegistries {
    public fun <T> itemComponentType(key: Key): ItemComponentType<T>
    public fun itemType(key: Key): ItemType
    public fun blockType(key: Key): BlockType
}
