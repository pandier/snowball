package io.github.pandier.snowball.event.player

import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.event.Event
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

/**
 * Called when a player leaves the server.
 */
public data class PlayerLeaveEvent(
    public val player: Player,
    public val originalMessage: Component?,
    public var audience: Audience,
) : Event {
    public var message: Component? = originalMessage
}
