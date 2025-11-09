package io.github.pandier.snowball.scoreboard

import io.github.pandier.snowball.entity.Entity
import org.jetbrains.annotations.UnmodifiableView

public interface Scoreboard {

    /**
     * An unmodifiable view to the teams in this scoreboard.
     */
    public val teams: @UnmodifiableView Collection<Team>

    /**
     * Gets a team by name. Returns null if not found.
     */
    public fun getTeam(name: String): Team?

    /**
     * Checks if this scoreboard has a team with the given [name].
     */
    public fun hasTeam(name: String): Boolean

    /**
     * Gets the team that a member with the given [name] belongs to. Returns null if not found.
     */
    public fun getTeamByMember(name: String): Team?

    /**
     * Gets the team that the given member [entity] belongs to. Returns null if not found.
     */
    public fun getTeamByMember(entity: Entity): Team? {
        return getTeamByMember(entity.scoreboardName)
    }

    /**
     * Creates a new team with the given [name].
     * If a team with the given name already exists, it is returned.
     */
    public fun createTeam(name: String): Team
}
