package io.github.pandier.snowball.impl.item

import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.item.ItemComponentType
import net.minecraft.component.ComponentType

class ItemComponentTypeImpl<T, V>(
    override val adaptee: ComponentType<V>,
    private val snowballMapper: (V) -> T,
    private val vanillaMapper: (T) -> V,
) : SnowballAdapter(adaptee), ItemComponentType<T> {
    fun snowball(value: V): T = snowballMapper(value)
    fun vanilla(value: T): V = vanillaMapper(value)
}
