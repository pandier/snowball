package io.github.pandier.snowball.impl.command

import io.github.pandier.snowball.command.CommandSource
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.World
import net.kyori.adventure.text.Component
import net.minecraft.commands.CommandSourceStack

class CommandSourceImpl(
    override val adaptee: CommandSourceStack,
) : SnowballAdapter(), CommandSource {
    override val name: String
        get() = adaptee.textName
    override val displayName: Component
        get() = adaptee.displayName.let(Conversions.Adventure::adventure)
    override val world: World
        get() = adaptee.level.let(Conversions::snowball)
    override val position: Vector3d
        get() = Vector3d(adaptee.position.x, adaptee.position.y, adaptee.position.z)
    override val rotation: Vector2f
        get() = Vector2f(adaptee.rotation.x, adaptee.rotation.y)
    override val entity: Entity
        get() = adaptee.entityOrException.let(Conversions::snowball)
    override val entityOrNull: Entity?
        get() = adaptee.entity?.let(Conversions::snowball)
    override val player: Player
        get() = adaptee.playerOrException.let(Conversions::snowball)
    override val playerOrNull: Player?
        get() = adaptee.player?.let(Conversions::snowball)

    override fun sendMessage(message: Component) {
        adaptee.sendSystemMessage(Conversions.Adventure.vanilla(message))
    }

    override fun sendError(message: Component) {
        adaptee.sendFailure(Conversions.Adventure.vanilla(message))
    }

    override fun sendFeedback(message: Component, broadcast: Boolean) {
        val message = Conversions.Adventure.vanilla(message)
        adaptee.sendSuccess({ message }, broadcast)
    }
}