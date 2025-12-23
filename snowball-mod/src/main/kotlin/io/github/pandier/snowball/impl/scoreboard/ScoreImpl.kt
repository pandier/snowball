package io.github.pandier.snowball.impl.scoreboard

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.scoreboard.NumberFormat
import io.github.pandier.snowball.scoreboard.Score
import net.kyori.adventure.text.Component

class ScoreImpl(
    override val adaptee: ExtendedScoreAccess
) : SnowballAdapter(), Score {

    override var value: Int
        get() = adaptee.get()
        set(value) { adaptee.set(value) }

    override var display: Component?
        get() = adaptee.display()?.let(Conversions.Adventure::adventure)
        set(value) { adaptee.display(value?.let(Conversions.Adventure::vanilla)) }

    override var numberFormat: NumberFormat?
        get() = adaptee.numberFormatOverride()?.let(Conversions::snowball)
        set(value) { adaptee.numberFormatOverride(value?.let(Conversions::vanilla)) }

    override var isLocked: Boolean
        get() = adaptee.locked()
        set(value) { if (value) adaptee.lock() else adaptee.unlock() }
}