package io.github.pandier.snowball.inventory

import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import io.github.pandier.snowball.item.ItemStacks
import io.github.pandier.snowball.item.ItemType

public interface Inventory : Iterable<ItemStackView> {
    public val size: Int

    /**
     * The maximum count an [ItemStack] in this inventory can have.
     */
    public val maxStackCount: Int

    public fun isEmpty(): Boolean =
        all { it.isEmpty() }

    public operator fun get(index: Int): ItemStack

    public operator fun set(index: Int, stack: ItemStackView)

    public fun remove(index: Int): ItemStack

    /**
     * Inserts the given [stack] into this inventory by first finding an existing stack to stack onto,
     * and then finding an empty index if necessary. The supplied [stack] **is modified** to reflect the
     * result of the operation. Returns the inserted amount.
     *
     * Implementations may slightly modify this behavior.
     *
     * ### Example
     *
     * ```
     * val stack = ItemStack.of(ItemTypes.DIAMOND, 10)
     * if (inventory.insert(stack) > 0) {
     *     if (stack.isEmpty()) {
     *         // The stack was fully inserted
     *     } else {
     *         // Only part of the stack was inserted
     *     }
     * }
     * ```
     */
    public fun insert(stack: ItemStack): Int {
        if (stack.isEmpty()) return 0

        val originalCount = stack.count

        // Find an existing item stack to stack onto
        if (stack.isStackable) {
            for (i in 0 until size) {
                val existingStack = get(i)
                if (!ItemStacks.canStack(existingStack, stack))
                    continue

                val maxCount = minOf(existingStack.maxCount, maxStackCount)
                val count = minOf(stack.count, maxCount - existingStack.count)
                if (count <= 0)
                    continue

                stack.count -= count

                val newStack = existingStack.copy()
                newStack.count += count
                set(i, newStack)

                if (stack.isEmpty())
                    return originalCount
            }
        }

        // Find an empty slot
        for (i in 0 until size) {
            val existingStack = get(i)
            if (!existingStack.isEmpty())
                continue

            val count = minOf(stack.count, stack.maxCount, maxStackCount)

            stack.count -= count

            val newStack = stack.copy()
            newStack.count = count
            set(i, newStack)

            if (stack.isEmpty())
                return originalCount
        }

        return originalCount - stack.count
    }

    public fun count(type: ItemType): Int =
        sumOf { if (it.type == type) it.count else 0 }

    public fun clear()
}
