package io.github.pandier.snowball.item

import io.github.pandier.snowball.Snowball

public interface ItemStack : ItemStackView {
    public companion object {
        @JvmStatic
        public fun empty(): ItemStack =
            Snowball.factories.emptyItemStack()

        @JvmStatic
        public fun of(type: ItemType, count: Int): ItemStack =
            Snowball.factories.itemStack(type, count)

        @JvmStatic
        public fun of(type: ItemType): ItemStack =
            Snowball.factories.itemStack(type, 1)
    }

    public override var count: Int

    public fun <T> set(type: ItemComponentType<T>, value: T?): T?

    public fun <T> reset(type: ItemComponentType<out T>): T?

    public fun <T> remove(type: ItemComponentType<out T>): T?
}
