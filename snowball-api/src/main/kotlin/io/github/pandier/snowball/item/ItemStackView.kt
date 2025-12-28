package io.github.pandier.snowball.item

import io.github.pandier.snowball.item.component.ItemComponentMap
import io.github.pandier.snowball.item.component.ItemComponentType
import io.github.pandier.snowball.item.component.ItemComponentTypes

/**
 * A view of an [ItemStack].
 */
public interface ItemStackView {
    public val type: ItemType
    public val count: Int
    public val components: ItemComponentMap
    public val defaultComponents: ItemComponentMap

    public val maxCount: Int
        get() = getOrElse(ItemComponentTypes.MAX_STACK_SIZE, 1)

    public val isStackable: Boolean
        get() = maxCount > 1

    public fun <T> get(type: ItemComponentType<out T>): T?

    public fun <T> getOrElse(type: ItemComponentType<out T>, default: T): T {
        return get(type) ?: default
    }

    public fun isEmpty(): Boolean

    public fun copy(): ItemStack
}
