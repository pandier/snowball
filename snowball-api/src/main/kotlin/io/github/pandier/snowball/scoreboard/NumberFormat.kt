package io.github.pandier.snowball.scoreboard

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style

/**
 * An [Objective] number format.
 */
public sealed interface NumberFormat {
    public companion object {
        @JvmStatic
        public fun blank(): Blank = Blank

        @JvmStatic
        public fun fixed(text: Component): Fixed = Fixed(text)

        @JvmStatic
        public fun styled(style: Style): Styled = Styled(style)
    }

    public object Blank : NumberFormat

    public class Fixed(public val text: Component) : NumberFormat

    public class Styled(public val style: Style) : NumberFormat
}