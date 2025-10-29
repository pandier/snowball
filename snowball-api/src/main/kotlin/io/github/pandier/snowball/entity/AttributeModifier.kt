package io.github.pandier.snowball.entity

import net.kyori.adventure.key.Key

/**
 * An entity attribute modifier.
 *
 * @see Attribute
 */
public data class AttributeModifier(
    public val key: Key,
    public val amount: Double,
    public val operation: Operation,
) {
    public enum class Operation {

        /**
         * Adds the modifier's value to the total value of the attribute.
         */
        ADD_VALUE,

        /**
         * Multiplies the modifier's value with the attribute's **base** value and adds it to the total value.
         */
        ADD_MULTIPLIED_BASE,

        /**
         * Multiplies the modifier's value with the attribute's **total** value and adds it to the total value.
         */
        ADD_MULTIPLIED_TOTAL,
    }
}
