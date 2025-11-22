package io.github.pandier.snowball.impl.inventory

import com.google.common.collect.Iterators
import io.github.pandier.snowball.inventory.Inventory
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView

open class ProxyInventory(
    val proxied: Inventory,
    val offset: Int,
    override val size: Int,
) : Inventory {
    override val maxStackCount: Int
        get() = proxied.maxStackCount

    override fun get(index: Int): ItemStack =
        proxied[index + offset]

    override fun remove(index: Int): ItemStack =
        proxied.remove(index + offset)

    override fun set(index: Int, stack: ItemStackView) =
        proxied.set(index + offset, stack)

    override fun insert(stack: ItemStack): Int =
        proxied.insert(stack)

    override fun clear() {
        for (i in 0 until size) {
            proxied.remove(i + offset)
        }
    }

    override fun iterator(): Iterator<ItemStack> {
        val iterator = proxied.iterator()
        Iterators.advance(iterator, offset)
        return Iterators.limit(iterator, size)
    }
}