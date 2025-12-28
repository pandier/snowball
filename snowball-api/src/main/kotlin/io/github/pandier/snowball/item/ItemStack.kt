package io.github.pandier.snowball.item

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.item.component.ItemComponentType
import java.util.function.Supplier

public interface ItemStack : ItemStackView {
    public companion object {
        @JvmStatic
        public fun empty(): ItemStack =
            Snowball.factories.emptyItemStack()

        @JvmStatic
        public fun of(type: Supplier<ItemType>): ItemStack =
            of(type.get())

        @JvmStatic
        public fun of(type: ItemType): ItemStack =
            Snowball.factories.itemStack(type, 1)

        @JvmStatic
        public fun of(type: Supplier<ItemType>, count: Int): ItemStack =
            of(type.get(), count)

        @JvmStatic
        public fun of(type: ItemType, count: Int): ItemStack =
            Snowball.factories.itemStack(type, count)
    }

    public override var count: Int

    public fun <T> set(type: ItemComponentType<T>, value: T?): T?

    public fun set(type: ItemComponentType<Unit>): Unit? {
        return set(type, Unit)
    }

    public fun <T> reset(type: ItemComponentType<out T>): T?

    public fun <T> remove(type: ItemComponentType<out T>): T?
}
