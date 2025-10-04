package io.github.pandier.snowball.impl.item

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.item.ItemComponentMap
import io.github.pandier.snowball.item.ItemComponentType
import net.minecraft.component.ComponentMap
import java.util.stream.Collectors

class ItemComponentMapImpl(
    override val adaptee: ComponentMap,
) : SnowballAdapter(adaptee), ItemComponentMap {
    override val size: Int
        get() = adaptee.size()
    override val types: Set<ItemComponentType<*>>
        get() = adaptee.types.stream().map(Conversions::snowball).collect(Collectors.toSet())

    override fun <T> get(type: ItemComponentType<out T>): T? =
        getInternal(type as ItemComponentTypeImpl<T, *>)

    private fun <T, V> getInternal(impl: ItemComponentTypeImpl<T, V>): T? =
        adaptee.get(impl.adaptee)?.let(impl::snowball)

    override fun contains(type: ItemComponentType<*>): Boolean =
        adaptee.contains((type as ItemComponentTypeImpl<*, *>).adaptee)

    override fun isEmpty(): Boolean =
        adaptee.isEmpty
}