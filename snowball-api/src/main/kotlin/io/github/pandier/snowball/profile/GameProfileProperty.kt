package io.github.pandier.snowball.profile

public data class GameProfileProperty(
    public val name: String,
    public val value: String,
    public val signature: String? = null,
)
