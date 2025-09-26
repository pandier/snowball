package io.github.pandier.snowball.inventory

import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import io.github.pandier.snowball.item.ItemType
import java.util.function.Consumer

public interface Inventory : Iterable<ItemStack> {
    public val size: Int

    /**
     * The maximum count an [ItemStack] in this inventory can have.
     */
    public val maxStackCount: Int

    public fun isEmpty(): Boolean =
        all { it.isEmpty() }

    public operator fun get(index: Int): ItemStackView

    public operator fun set(index: Int, stack: ItemStackView)

    public fun modify(index: Int, action: Consumer<ItemStack>): ItemStack {
        return get(index).copy()
            .also(action::accept)
            .also { set(index, it) }
    }

    public fun remove(index: Int): ItemStack

    public fun isValid(index: Int, stack: ItemStack): Boolean

    public fun count(type: ItemType): Int =
        sumOf { if (it.type == type) it.count else 0 }

    public fun clear()
}
