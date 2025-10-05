package io.github.pandier.snowball.impl.inventory

import com.google.common.collect.Iterators
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.impl.item.ItemTypeImpl
import io.github.pandier.snowball.inventory.Inventory
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import io.github.pandier.snowball.item.ItemType
import net.minecraft.world.Container

open class InventoryImpl(
    adaptee: Container
) : SnowballAdapter(adaptee), Inventory {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: Container = adaptee

    override val size: Int
        get() = adaptee.containerSize

    override val maxStackCount: Int
        get() = adaptee.maxCountPerStack

    override fun isEmpty(): Boolean =
        adaptee.isEmpty

    override fun get(index: Int): ItemStackView =
        adaptee.getItem(index).let(Conversions::snowball)

    override fun remove(index: Int): ItemStack =
        adaptee.removeItemNoUpdate(index).let(Conversions::snowball)

    override fun set(index: Int, stack: ItemStackView) =
        adaptee.setItem(index, (stack.copy() as ItemStackImpl).adaptee)

    override fun count(type: ItemType): Int =
        adaptee.countItem((type as ItemTypeImpl).adaptee)

    override fun clear() =
        adaptee.clearContent()

    override fun iterator(): Iterator<ItemStack> =
        Iterators.transform(adaptee.iterator(), Conversions::snowball)
}