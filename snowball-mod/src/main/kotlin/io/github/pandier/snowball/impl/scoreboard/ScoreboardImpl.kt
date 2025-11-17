package io.github.pandier.snowball.impl.scoreboard

import com.google.common.collect.Collections2
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.scoreboard.Criterion
import io.github.pandier.snowball.scoreboard.DisplaySlot
import io.github.pandier.snowball.scoreboard.Objective
import io.github.pandier.snowball.scoreboard.Scoreboard
import io.github.pandier.snowball.scoreboard.Team
import net.minecraft.network.chat.Component
import org.jetbrains.annotations.UnmodifiableView

class ScoreboardImpl(
    override val adaptee: net.minecraft.world.scores.Scoreboard
) : SnowballAdapter(adaptee), Scoreboard {

    override val objectives: @UnmodifiableView Collection<Objective>
        get() = Collections2.transform(adaptee.objectives, Conversions::snowball)

    override fun getObjective(name: String): Objective? {
        return adaptee.getObjective(name)?.let(Conversions::snowball)
    }

    override fun hasObjective(name: String): Boolean {
        return adaptee.getObjective(name) != null
    }

    override fun createObjective(name: String, criterion: Criterion): Objective {
        getObjective(name)?.let { return it }
        val vCriterion = (criterion as CriterionImpl).adaptee
        val vObjective = adaptee.addObjective(name, vCriterion,
            Component.literal(name), vCriterion.defaultRenderType, false, null)
        return vObjective.let(Conversions::snowball)
    }

    override fun getDisplaySlot(slot: DisplaySlot): Objective? {
        return adaptee.getDisplayObjective(slot.let(Conversions::vanilla))?.let(Conversions::snowball)
    }

    override fun setDisplaySlot(slot: DisplaySlot, objective: Objective?) {
        if (objective == null) {
            clearDisplaySlot(slot)
            return
        }

        val vanilla = (objective as ObjectiveImpl).adaptee
        if (vanilla.scoreboard != adaptee) throw IllegalArgumentException("Objective does not belong to this scoreboard")
        adaptee.setDisplayObjective(slot.let(Conversions::vanilla), vanilla)
    }

    override fun clearDisplaySlot(slot: DisplaySlot) {
        adaptee.setDisplayObjective(slot.let(Conversions::vanilla), null)
    }

    override val teams: @UnmodifiableView Collection<Team>
        get() = Collections2.transform(adaptee.playerTeams, Conversions::snowball)

    override fun getTeam(name: String): Team? {
        return adaptee.getPlayerTeam(name)?.let(Conversions::snowball)
    }

    override fun hasTeam(name: String): Boolean {
        return adaptee.getPlayerTeam(name) != null
    }

    override fun getTeamByMember(name: String): Team? {
        return adaptee.getPlayersTeam(name)?.let(Conversions::snowball)
    }

    override fun createTeam(name: String): Team {
        getTeam(name)?.let { return it }
        return adaptee.addPlayerTeam(name).let(Conversions::snowball)
    }
}