package io.github.pandier.snowball.event.player

import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.event.Event
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

/**
 * Called when a player joins the server after they are properly added into the world.
 * To modify the player's initial location or world, use [PlayerPrepareEvent].
 */
public class PlayerJoinEvent(
    public val player: Player,
    public val originalMessage: Component?,
    public var audience: Audience,
) : Event {
    public var message: Component? = originalMessage
}
