package io.github.pandier.snowball.entity

/**
 * An AI-driven [LivingEntity].
 */
public interface Mob : LivingEntity {

    /**
     * Specifies if the mob must not despawn naturally.
     */
    public var isPersistent: Boolean

    /**
     * Specifies if the mob's AI is enabled.
     * If disabled, the mob won't move at all, including gravity.
     */
    public var hasAI: Boolean
}