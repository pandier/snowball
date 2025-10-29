package io.github.pandier.snowball.entity

import net.kyori.adventure.key.Key

/**
 * Represents a [LivingEntity] attribute with a value that's calculated using a base value and [AttributeModifier]s.
 *
 * ### Modifiers
 *
 * An attribute can have multiple modifiers identified by a [Key].
 * These modifiers execute [operations][AttributeModifier.Operation] on the base value of the attribute
 * to calculate the final [value]. Modifiers are executed in order of operations.
 */
public interface Attribute {

    /**
     * Type of the attribute.
     */
    public val type: AttributeType

    /**
     * A collection of all the attribute's modifiers.
     */
    public val modifiers: Collection<AttributeModifier>

    /**
     * The base value of the attribute.
     */
    public var baseValue: Double

    /**
     * The final calculated value of the attribute.
     */
    public val value: Double

    /**
     * Adds a new modifier to this attribute.
     * Nothing happens if an attribute with the same key already exsts.
     *
     * @return true if added successfully (the key was not occupied)
     */
    public fun addModifier(modifier: AttributeModifier): Boolean

    /**
     * Removes an existing modifier with the given [key].
     *
     * @return true if the modifier existed and was removed
     */
    public fun removeModifier(key: Key): Boolean

    /**
     * Returns a modifier with the given [key] or null if it doesn't exist.
     */
    public fun getModifier(key: Key): AttributeModifier?

    /**
     * Checks if a modifier with the given [key] exists.
     */
    public fun hasModifier(key: Key): Boolean

    /**
     * Clears all modifiers.
     */
    public fun clearModifiers()
}