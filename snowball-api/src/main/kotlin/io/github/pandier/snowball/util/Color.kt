package io.github.pandier.snowball.util

import net.kyori.adventure.util.RGBLike
import org.jetbrains.annotations.Range

public data class Color(public val rgb: Int) : RGBLike {
    public constructor(red: Int, green: Int, blue: Int) : this(((red and 0xFF) shl 16) or ((green and 0xFF) shl 8) or (blue and 0xFF))

    public companion object {
        @JvmStatic
        public fun of(rgb: Int): Color =
            Color(rgb)

        @JvmStatic
        public fun of(red: Int, green: Int, blue: Int): Color =
            Color(red, green, blue)

        @JvmStatic
        public fun of(color: java.awt.Color): Color =
            of(color.rgb)

        @JvmStatic
        public fun of(color: RGBLike): Color =
            of(color.red(), color.green(), color.blue())
    }

    override fun red(): @Range(from = 0, to = 255) Int =
        (rgb shr 16) and 0xFF

    override fun green(): @Range(from = 0, to = 255) Int =
        (rgb shr 8) and 0xFF

    override fun blue(): @Range(from = 0, to = 255) Int =
        rgb and 0xFF
}
