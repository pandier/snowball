package io.github.pandier.snowball.scoreboard

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.player.Player
import org.jetbrains.annotations.UnmodifiableView
import java.util.function.Supplier

public interface Scoreboard {
    public companion object {

        /**
         * Creates a new [Scoreboard] instance that can be set for individual players with [Player.scoreboard].
         */
        @JvmStatic
        public fun create(): Scoreboard = Snowball.factories.scoreboard()
    }

    /**
     * An unmodifiable view to the objectives in this scoreboard.
     */
    public val objectives: @UnmodifiableView Collection<Objective>

    /**
     * Gets an objective by name. Returns null if not found.
     */
    public fun getObjective(name: String): Objective?

    /**
     * Checks if this scoreboard has an objective with the given [name].
     */
    public fun hasObjective(name: String): Boolean

    /**
     * Creates a new objective with the given [name] and [criterion].
     *
     * If an objective with the given already exists, it is returned.
     */
    public fun createObjective(name: String, criterion: Criterion): Objective

    public fun createObjective(name: String, criterion: Supplier<Criterion>): Objective {
        return createObjective(name, criterion.get())
    }

    /**
     * Gets the objective for the given display [slot]. Returns null if none set.
     */
    public fun getDisplaySlot(slot: DisplaySlot): Objective?

    /**
     * Sets an objective for the given display [slot]. Can be null to clear the slot.
     *
     * @throws IllegalArgumentException if the objective doesn't belong to this scoreboard
     */
    public fun setDisplaySlot(slot: DisplaySlot, objective: Objective?)

    /**
     * Clears the given display [slot].
     */
    public fun clearDisplaySlot(slot: DisplaySlot)

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
     *
     * If a team with the given name already exists, it is returned.
     */
    public fun createTeam(name: String): Team

    /**
     * An unmodifiable view to the collection of players that are viewing this scoreboard.
     */
    public val viewers: @UnmodifiableView Collection<Player>
}
