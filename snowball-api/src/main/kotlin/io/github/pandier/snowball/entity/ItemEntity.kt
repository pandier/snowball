package io.github.pandier.snowball.entity

import io.github.pandier.snowball.item.ItemStack
import java.util.UUID

/**
 * An entity that stores an [ItemStack].
 */
public interface ItemEntity : Entity {

    /**
     * The [ItemStack] stored in this item entity.
     */
    public var itemStack: ItemStack

    /**
     * The entity that threw or dropped the item.
     */
    public var thrower: Entity?

    /**
     * A uuid of a player that can pickup this item.
     * Everyone can pick up the item if set to null.
     */
    public var target: UUID?

    /**
     * A value between [Short.MIN_VALUE] and [Short.MAX_VALUE] representing how many ticks
     * this item has been existing for. The item is removed when this value reaches 6000.
     *
     * [Short.MIN_VALUE] represents an infinite lifetime.
     */
    public var age: Int

    /**
     * A value between [Short.MIN_VALUE] and [Short.MAX_VALUE] representing how many ticks
     * this item cannot be picked up for. It's decreased by 1 every tick until it reaches zero.
     *
     * [Short.MAX_VALUE] represents an infinite pickup delay.
     */
    public var pickupDelay: Int

    /**
     * Returns true if the [age] represents an infinite value.
     */
    public val isInfiniteLifetime: Boolean

    /**
     * Returns true if the [pickupDelay] represents na infinite value.
     */
    public val isInfinitePickupDelay: Boolean

    /**
     * Sets [age] to an infinite value.
     */
    public fun setInfiniteLifetime()

    /**
     * Sets [pickupDelay] to an infinite value.
     */
    public fun setInfinitePickupDelay()

    /**
     * Sets the [velocity] and [pickupDelay] to values used when dropping items.
     */
    public fun setNaturalDropProperties()
}