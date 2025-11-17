package io.github.pandier.snowball.impl.scoreboard

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.scoreboard.Criterion
import io.github.pandier.snowball.scoreboard.NumberFormat
import io.github.pandier.snowball.scoreboard.Objective
import io.github.pandier.snowball.scoreboard.Score
import net.kyori.adventure.text.Component
import net.minecraft.world.scores.ScoreHolder

class ObjectiveImpl(
    override val adaptee: net.minecraft.world.scores.Objective
) : SnowballAdapter(adaptee), Objective {

    override val name: String
        get() = adaptee.name

    override val criterion: Criterion
        get() = adaptee.criteria.let(Conversions::snowball)

    override var displayName: Component
        get() = adaptee.displayName.let(Conversions.Adventure::adventure)
        set(value) { adaptee.displayName = Conversions.Adventure.vanilla(value) }

    override var renderType: Objective.RenderType
        get() = adaptee.renderType.let(Conversions::snowball)
        set(value) { adaptee.renderType = value.let(Conversions::vanilla) }

    override var displayAutoUpdate: Boolean
        get() = adaptee.displayAutoUpdate()
        set(value) { adaptee.setDisplayAutoUpdate(value) }

    override var numberFormat: NumberFormat?
        get() = adaptee.numberFormat()?.let(Conversions::snowball)
        set(value) { adaptee.setNumberFormat(value?.let(Conversions::vanilla)) }

    override val entries: List<String>
        get() = adaptee.scoreboard.listPlayerScores(adaptee).map { it.owner }

    override fun getScore(name: String): Score? {
        if (!hasScore(name)) return null
        return createScore(name)
    }

    override fun hasScore(name: String): Boolean {
        return adaptee.scoreboard.getPlayerScoreInfo(ScoreHolder.forNameOnly(name), adaptee) != null
    }

    override fun createScore(name: String): Score {
        if (isRemoved()) error("Cannot modify objective '$name' because it has been removed")
        return adaptee.scoreboard.getOrCreatePlayerScore(ScoreHolder.forNameOnly(name), adaptee)
            .let { Conversions.snowball(it as ExtendedScoreAccess) }
    }

    override fun removeScore(name: String) {
        if (isRemoved()) error("Cannot modify objective '$name' because it has been removed")
        adaptee.scoreboard.resetSinglePlayerScore(ScoreHolder.forNameOnly(name), adaptee)
    }

    override fun remove() {
        if (isRemoved()) return
        adaptee.scoreboard.removeObjective(adaptee)
    }

    private fun isRemoved(): Boolean {
        return adaptee.scoreboard.getObjective(adaptee.name) == null
    }
}