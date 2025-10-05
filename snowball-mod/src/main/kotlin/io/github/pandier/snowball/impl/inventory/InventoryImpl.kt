package io.github.pandier.snowball.impl.inventory

import com.google.common.collect.Iterators
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.impl.item.ItemTypeImpl
import io.github.pandier.snowball.inventory.Inventory
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemType

open class InventoryImpl(
    adaptee: net.minecraft.inventory.Inventory
) : SnowballAdapter(adaptee), Inventory {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: net.minecraft.inventory.Inventory = adaptee

    override val size: Int
        get() = adaptee.size()

    override fun isEmpty(): Boolean =
        adaptee.isEmpty

    override fun get(index: Int): ItemStack =
        adaptee.getStack(index).let(Conversions::snowball)

    override fun remove(index: Int): ItemStack =
        adaptee.removeStack(index).let(Conversions::snowball)

    override fun set(index: Int, stack: ItemStack) =
        adaptee.setStack(index, (stack as ItemStackImpl).adaptee)

    override fun isValid(index: Int, stack: ItemStack): Boolean =
        adaptee.isValid(index, (stack as ItemStackImpl).adaptee)

    override fun count(type: ItemType): Int =
        adaptee.count((type as ItemTypeImpl).adaptee)

    override fun clear() =
        adaptee.clear()

    override fun iterator(): Iterator<ItemStack> =
        Iterators.transform(adaptee.iterator(), Conversions::snowball)
}