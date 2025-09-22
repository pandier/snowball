package io.github.pandier.snowball.item

import io.github.pandier.snowball.Snowball

/**
 * A read-only view of an [ItemStack].
 * It is not guaranteed that the underlying [ItemStack] will not change,
 * but it is also not guaranteed that it can change.
 */
public interface ItemStackView {
    public companion object {
        @JvmStatic
        public fun empty(): ItemStackView =
            Snowball.factories.emptyItemStack()
    }

    public val type: ItemType
    public val count: Int

    public fun <T> get(type: ItemComponentType<out T>): T?

    public fun <T> getOrDefault(type: ItemComponentType<out T>, default: T): T {
        return get(type) ?: default
    }

    public fun isEmpty(): Boolean

    public fun copy(): ItemStack
}
