package io.github.pandier.snowball.world.block

import io.github.pandier.snowball.item.ItemType

public interface BlockType {

    public val defaultBlockState: BlockState

    /**
     * Returns the [ItemType] that represents this block.
     * Can be [AIR][io.github.pandier.snowball.item.ItemTypes.AIR].
     */
    public val itemType: ItemType
}
