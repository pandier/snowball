package io.github.pandier.snowball.impl.item

import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.item.ItemTypes
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
internal object EmptyItemStack : ItemStackImpl(net.minecraft.item.ItemStack.EMPTY) {
    override var count: Int
        get() = 0
        set(value) {}

    override val type: ItemType
        get() = ItemTypes.AIR

    override fun <T> set(type: ItemComponentType<T>, value: T?): T? {
        return null
    }

    override fun <T> remove(type: ItemComponentType<out T>): T? {
        return null
    }

    override fun <T> get(type: ItemComponentType<out T>): T? {
        return null
    }

    override fun <T> reset(type: ItemComponentType<out T>): T? {
        return null
    }

    override fun isEmpty(): Boolean {
        return true
    }

    override fun copy(): ItemStack {
        return ItemStack.of(type)
    }
}