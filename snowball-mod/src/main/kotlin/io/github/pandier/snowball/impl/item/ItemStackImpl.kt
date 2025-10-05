package io.github.pandier.snowball.impl.item

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemType

class ItemStackImpl(
    override val adaptee: net.minecraft.world.item.ItemStack,
) : SnowballAdapter(adaptee), ItemStack {
    companion object {
        @JvmField val EMPTY = ItemStackImpl(net.minecraft.world.item.ItemStack.EMPTY)
    }

    override var count: Int
        get() = adaptee.count
        set(value) {
            adaptee.count = value
        }

    override val type: ItemType
        get() = Conversions.snowball(adaptee.item)

    override fun <T> set(type: ItemComponentType<T>, value: T?): T? =
        setInternal(type as ItemComponentTypeImpl<T, *>, value)

    private fun <T, V> setInternal(impl: ItemComponentTypeImpl<T, V>, value: T?): T? =
        adaptee.set(impl.adaptee, value?.let(impl::vanilla))?.let(impl::snowball)

    override fun <T> remove(type: ItemComponentType<out T>): T? =
        removeInternal(type as ItemComponentTypeImpl<T, *>)

    private fun <T, V> removeInternal(impl: ItemComponentTypeImpl<T, V>): T? =
        adaptee.remove(impl.adaptee)?.let(impl::snowball)

    override fun <T> get(type: ItemComponentType<out T>): T? =
        getInternal(type as ItemComponentTypeImpl<T, *>)

    private fun <T, V> getInternal(impl: ItemComponentTypeImpl<T, V>): T? =
        adaptee.get(impl.adaptee)?.let(impl::snowball)

    override fun isEmpty(): Boolean =
        adaptee.isEmpty

    override fun copy(): ItemStack =
        ItemStackImpl(adaptee.copy())
}
