package io.github.pandier.snowball.item.component

public interface ItemComponentMap {
    public val size: Int

    public val types: Set<ItemComponentType<*>>

    public operator fun <T> get(type: ItemComponentType<out T>): T?

    public fun <T> getOrElse(type: ItemComponentType<out T>, default: T): T {
        return get(type) ?: default
    }

    public fun contains(type: ItemComponentType<*>): Boolean

    public fun isEmpty(): Boolean
}