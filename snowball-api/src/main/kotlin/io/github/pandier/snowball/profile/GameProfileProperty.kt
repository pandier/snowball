package io.github.pandier.snowball.profile

/**
 * Represents a property of a [GameProfile].
 */
public data class GameProfileProperty(
    public val name: String,
    public val value: String,
    public val signature: String? = null,
) {
    public companion object {
        @JvmStatic
        public fun of(name: String, value: String): GameProfileProperty =
            GameProfileProperty(name, value)

        @JvmStatic
        public fun of(name: String, value: String, signature: String?): GameProfileProperty =
            GameProfileProperty(name, value, signature)
    }
}
