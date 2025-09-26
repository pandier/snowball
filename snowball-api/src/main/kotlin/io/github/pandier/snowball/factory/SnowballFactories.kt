package io.github.pandier.snowball.factory

import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemType

public interface SnowballFactories {
    public fun emptyItemStack(): ItemStack
    public fun itemStack(type: ItemType, count: Int): ItemStack
}
