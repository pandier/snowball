package io.github.pandier.snowball.item.component

import io.github.pandier.snowball.math.Color

public data class FireworkExplosion(
    public val shape: Shape = Shape.SMALL_BALL,
    public val colors: List<Color> = listOf(),
    public val fadeColors: List<Color> = listOf(),
    public val isTrail: Boolean = false,
    public val isTwinkle: Boolean = false,
) {
    public enum class Shape {
        SMALL_BALL,
        LARGE_BALL,
        STAR,
        CREEPER,
        BURST,
    }

    public companion object {
        @JvmField
        public val DEFAULT: FireworkExplosion = FireworkExplosion()

        @JvmStatic
        public fun of(shape: Shape): FireworkExplosion =
            FireworkExplosion(shape)

        @JvmStatic
        public fun of(shape: Shape, colors: List<Color>): FireworkExplosion =
            FireworkExplosion(shape, colors)

        @JvmStatic
        public fun of(shape: Shape, colors: List<Color>, fadeColors: List<Color>): FireworkExplosion =
            FireworkExplosion(shape, colors, fadeColors)

        @JvmStatic
        public fun of(shape: Shape, colors: List<Color>, fadeColors: List<Color>, isTrail: Boolean, isTwinkle: Boolean): FireworkExplosion =
            FireworkExplosion(shape, colors, fadeColors, isTrail, isTwinkle)
    }
}