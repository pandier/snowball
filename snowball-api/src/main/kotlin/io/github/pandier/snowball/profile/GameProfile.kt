package io.github.pandier.snowball.profile

import java.util.UUID

public data class GameProfile(
    public val uuid: UUID,
    public val name: String,
    public val properties: List<GameProfileProperty> = emptyList(),
)
