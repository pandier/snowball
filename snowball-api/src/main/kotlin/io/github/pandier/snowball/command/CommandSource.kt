package io.github.pandier.snowball.command

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.World
import net.kyori.adventure.text.Component

public interface CommandSource {
    public val name: String
    public val displayName: Component
    public val world: World
    public val position: Vector3d
    public val rotation: Vector2f
    public val entity: Entity
    public val entityOrNull: Entity?
    public val player: Player
    public val playerOrNull: Player?

    public fun sendMessage(message: Component)

    public fun sendError(message: Component)

    public fun sendFeedback(message: Component, broadcast: Boolean)

    public fun sendFeedback(message: Component) {
        sendFeedback(message, false)
    }
}
