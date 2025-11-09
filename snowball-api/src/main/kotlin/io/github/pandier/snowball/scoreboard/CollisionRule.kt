package io.github.pandier.snowball.scoreboard

/**
 * Represents how collision between entities is applied.
 */
public enum class CollisionRule(
    public val isMembers: Boolean,
    public val isOthers: Boolean,
) {
    /**
     * Everyone has collision.
     */
    ALWAYS(true, true),

    /**
     * Only members of different teams have collision.
     */
    PUSH_OTHER_TEAMS(false, true),

    /**
     * Only members of the same team have collision.
     */
    PUSH_OWN_TEAM(true, false),

    /**
     * No collision.
     */
    NEVER(false, false),
}
