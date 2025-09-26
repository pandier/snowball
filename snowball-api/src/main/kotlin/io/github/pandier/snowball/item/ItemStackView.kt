package io.github.pandier.snowball.item

/**
 * A read-only view of an [ItemStack].
 * It is not guaranteed that the underlying [ItemStack] will not change,
 * but it is also not guaranteed that it can change.
 */
public interface ItemStackView {
    public val type: ItemType
    public val count: Int
    public val components: Set<ItemComponentType<*>>

    public val maxCount: Int
        get() = getOrElse(ItemComponentTypes.MAX_STACK_SIZE, 1)

    public fun <T> get(type: ItemComponentType<out T>): T?

    public fun <T> getOrElse(type: ItemComponentType<out T>, default: T): T {
        return get(type) ?: default
    }

    public fun <T> getDefault(type: ItemComponentType<out T>): T?

    public fun isEmpty(): Boolean

    public fun copy(): ItemStack
}
