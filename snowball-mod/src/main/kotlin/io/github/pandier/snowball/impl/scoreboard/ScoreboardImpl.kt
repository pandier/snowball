package io.github.pandier.snowball.impl.scoreboard

import com.google.common.collect.Collections2
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.scoreboard.Scoreboard
import io.github.pandier.snowball.scoreboard.Team
import org.jetbrains.annotations.UnmodifiableView

class ScoreboardImpl(
    override val adaptee: net.minecraft.world.scores.Scoreboard
) : SnowballAdapter(adaptee), Scoreboard {

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