package io.github.pandier.snowball.profile

import net.kyori.adventure.identity.Identity
import java.util.UUID

/**
 * Represents a player's game profile consisting of a [uuid], [name]
 * and some additional [properties] like the skin texture.
 */
public data class GameProfile(
    public val uuid: UUID,
    public val name: String,
    public val properties: List<GameProfileProperty> = emptyList(),
) : Identity {
    public companion object {
        @JvmStatic
        public fun of(uuid: UUID, name: String): GameProfile =
            GameProfile(uuid, name)

        @JvmStatic
        public fun of(uuid: UUID, name: String, properties: List<GameProfileProperty>): GameProfile =
            GameProfile(uuid, name, properties)
    }

    override fun uuid(): UUID = uuid
}
