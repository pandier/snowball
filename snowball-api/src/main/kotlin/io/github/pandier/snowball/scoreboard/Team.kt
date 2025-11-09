package io.github.pandier.snowball.scoreboard

import io.github.pandier.snowball.entity.Entity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.jetbrains.annotations.UnmodifiableView

public interface Team {

    /**
     * The name of the team.
     */
    public val name: String

    /**
     * The team's name displayed to players.
     */
    public var displayName: Component

    /**
     * The color of the team used for coloring the name of a member or the glowing effect.
     */
    public var color: NamedTextColor?

    /**
     * The prefix displayed before a member's name.
     */
    public var prefix: Component

    /**
     * The suffix displayed after a member's name.
     */
    public var suffix: Component

    /**
     * Whether friendly fire is enabled and team member's can attack each other.
     */
    public var allowFriendlyFire: Boolean

    /**
     * Whether team member's can see each other when invisible.
     */
    public var canSeeFriendlyInvisible: Boolean

    /**
     * The [Visibility] of name tags above members of this team.
     */
    public var nameTagVisibility: Visibility

    /**
     * The [Visibility] of death messages of members.
     */
    public var deathMessageVisibility: Visibility

    /**
     * The [CollisionRule] for members of the team.
     */
    public var collisionRule: CollisionRule

    /**
     * An unmodifiable view to the of members in this team.
     */
    public val members: @UnmodifiableView Collection<String>

    /**
     * Adds a new member with the given [name] to this team.
     *
     * If the name is already a member of a different team,
     * they're removed from that team and then added to this team.
     *
     * @throws IllegalStateException if this team has been removed
     */
    public fun addMember(name: String)

    public fun addMember(entity: Entity) {
        addMember(entity.scoreboardName)
    }

    /**
     * Removes a member with the given [name] from this team.
     *
     * @throws IllegalStateException if this team has been removed
     */
    public fun removeMember(name: String)

    public fun removeMember(entity: Entity) {
        removeMember(entity.scoreboardName)
    }

    /**
     * Removes this team from the scoreboard.
     */
    public fun remove()
}