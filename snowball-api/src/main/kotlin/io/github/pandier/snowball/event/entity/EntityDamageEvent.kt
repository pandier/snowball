package io.github.pandier.snowball.event.entity

import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.event.ActionEvent
import io.github.pandier.snowball.event.AllowEvent
import io.github.pandier.snowball.event.Event

/**
 * A multi-stage event for when an entity is damaged.
 */
// TODO: Damage modifiers
public interface EntityDamageEvent : Event {

    /**
     * The receiver of the damage.
     */
    public val entity: LivingEntity

    /**
     * Details about the source of the damage.
     */
    public val source: DamageSource

    /**
     * The base amount of damage before any calculations.
     */
    public val baseAmount: Float

    /**
     * The amount after calculations.
     */
    public val amount: Float

    /**
     * Determines if damaging should be allowed.
     *
     * @see AllowEvent
     */
    public interface Allow : AllowEvent, EntityDamageEvent

    /**
     * Called before inflicting the damage. Default can be prevented.
     *
     * @see ActionEvent
     */
    public interface Action : ActionEvent, EntityDamageEvent {

        /**
         * The amount after calculations.
         * This can be zero and the entity will still receive knockback and all the other stuff.
         *
         * Can be modified, but if the action is prevented, no damage will be inflicted.
         */
        override var amount: Float
    }

    /**
     * Called if damaging was denied.
     */
    public interface Canceled : EntityDamageEvent

    /**
     * Called after inflicting the damage.
     */
    public interface Post : EntityDamageEvent
}