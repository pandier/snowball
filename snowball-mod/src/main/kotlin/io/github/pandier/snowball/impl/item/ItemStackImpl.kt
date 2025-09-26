package io.github.pandier.snowball.impl.item

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.bridge.ResetableComponentAccessBridge
import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemType
import java.util.stream.Collectors

open class ItemStackImpl(
    override val adaptee: net.minecraft.item.ItemStack,
) : SnowballAdapter(adaptee), ItemStack {
    override var count: Int
        get() = adaptee.count
        set(value) {
            adaptee.count = value
        }

    override val type: ItemType
        get() = Conversions.snowball(adaptee.item)

    override val components: Set<ItemComponentType<*>>
        get() = adaptee.components.types.stream().map(Conversions::snowball).collect(Collectors.toSet())

    override fun <T> set(type: ItemComponentType<T>, value: T?): T? =
        setInternal(type as ItemComponentTypeImpl<T, *>, value)

    private fun <T, V> setInternal(impl: ItemComponentTypeImpl<T, V>, value: T?): T? =
        adaptee.set(impl.adaptee, value?.let(impl::vanilla))
            ?.takeIf { !isEmpty() } // Return null if empty
            ?.let(impl::snowball)

    override fun <T> reset(type: ItemComponentType<out T>): T? =
        resetInternal(type as ItemComponentTypeImpl<T, *>)

    @Suppress("CAST_NEVER_SUCCEEDS")
    private fun <T, V> resetInternal(impl: ItemComponentTypeImpl<T, V>): T? =
        (adaptee as ResetableComponentAccessBridge)
            .`snowball$reset`(impl.adaptee)
            ?.takeIf { !isEmpty() } // Return null if empty
            ?.let(impl::snowball)

    override fun <T> remove(type: ItemComponentType<out T>): T? =
        removeInternal(type as ItemComponentTypeImpl<T, *>)

    private fun <T, V> removeInternal(impl: ItemComponentTypeImpl<T, V>): T? =
        adaptee.remove(impl.adaptee)
            ?.takeIf { !isEmpty() } // Return null if empty
            ?.let(impl::snowball)

    override fun <T> get(type: ItemComponentType<out T>): T? =
        getInternal(type as ItemComponentTypeImpl<T, *>)

    private fun <T, V> getInternal(impl: ItemComponentTypeImpl<T, V>): T? =
        adaptee.get(impl.adaptee)?.let(impl::snowball)

    override fun <T> getDefault(type: ItemComponentType<out T>): T? =
        getDefaultInternal(type as ItemComponentTypeImpl<T, *>)

    private fun <T, V> getDefaultInternal(impl: ItemComponentTypeImpl<T, V>): T? =
        adaptee.defaultComponents.get(impl.adaptee)?.let(impl::snowball)

    override fun isEmpty(): Boolean =
        adaptee.isEmpty

    override fun copy(): ItemStack =
        ItemStackImpl(adaptee.copy())
}
