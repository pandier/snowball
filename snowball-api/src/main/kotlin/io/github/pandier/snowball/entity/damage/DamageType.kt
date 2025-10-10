package io.github.pandier.snowball.entity.damage

/**
 * A type of damage.
 *
 * @see DamageSource
 */
public interface DamageType {

    /**
     * The translation id of the damage type used for constructing translation keys.
     */
    public val translationId: String

    /**
     * The amount of exhaustion a player will receive after being hurt with this damage type.
     */
    public val exhaustion: Float
}
