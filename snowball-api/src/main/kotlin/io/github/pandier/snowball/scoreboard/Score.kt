package io.github.pandier.snowball.scoreboard

import net.kyori.adventure.text.Component

/**
 * A single entry inside an [Objective].
 * Consists of a [value] and some additional properties.
 */
public interface Score {

    /**
     * The value of the score that appears on display slots and is used for sorting in the sidebar.
     */
    public var value: Int

    /**
     * The customized display of the score, replacing the entry name.
     */
    public var display: Component?

    /**
     * The customized [NumberFormat] of the score.
     */
    public var numberFormat: NumberFormat?

    /**
     * The locked state of the score.
     *
     * If the score is locked (default), the `/trigger` command cannot be used on it.
     */
    public var isLocked: Boolean
}