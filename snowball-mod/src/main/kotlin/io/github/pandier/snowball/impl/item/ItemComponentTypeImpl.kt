package io.github.pandier.snowball.impl.item

import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.item.ItemComponentType
import net.minecraft.core.component.DataComponentType

class ItemComponentTypeImpl<T, V>(
    override val adaptee: DataComponentType<V>,
    val type: Class<T>?,
    private val snowballMapper: (V) -> T,
    private val vanillaMapper: (T) -> V,
) : SnowballAdapter(adaptee), ItemComponentType<T> {
    fun snowball(value: V): T = snowballMapper(value)
    fun vanilla(value: T): V = vanillaMapper(value)
}
