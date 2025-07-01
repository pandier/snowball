package io.github.pandier.snowball.command

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.Player
import net.kyori.adventure.text.Component

public interface CommandSource {
    public val entity: Entity
    public val entityOrNull: Entity?
    public val player: Player
    public val playerOrNull: Player?

    public fun sendMessage(message: Component)
}
