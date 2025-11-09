package io.github.pandier.snowball.scoreboard

/**
 * Represents the visiblity of certain [Team] properties.
 */
public enum class Visibility(
    public val isMembers: Boolean,
    public val isOthers: Boolean,
) {
    /**
     * Visible to everyone.
     */
    ALWAYS(true, true),

    /**
     * Visible only to members of the same team.
     */
    HIDE_FOR_OTHER_TEAMS(true, false),

    /**
     * Visible to everyone except members of the same team.
     */
    HIDE_FOR_OWN_TEAM(false, true),

    /**
     * Hidden.
     */
    NEVER(false, false),
}
