package io.github.pandier.snowball.inventory

import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemType

public interface Inventory : Iterable<ItemStack> {
    public val size: Int

    public fun isEmpty(): Boolean =
        all { it.isEmpty() }

    public operator fun get(index: Int): ItemStack

    public operator fun set(index: Int, stack: ItemStack)

    public fun remove(index: Int): ItemStack

    public fun isValid(index: Int, stack: ItemStack): Boolean

    public fun count(type: ItemType): Int =
        sumOf { if (it.type == type) it.count else 0 }

    public fun clear()
}
