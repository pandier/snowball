package io.github.pandier.snowball.impl.command

import io.github.pandier.snowball.command.CommandSource
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.World
import net.kyori.adventure.text.Component
import net.minecraft.server.command.ServerCommandSource

class CommandSourceImpl(
    override val adaptee: ServerCommandSource,
) : SnowballAdapter(adaptee), CommandSource {
    override val world: World
        get() = adaptee.world.let(Conversions::world)
    override val position: Vector3d
        get() = Vector3d(adaptee.position.x, adaptee.position.y, adaptee.position.z)
    override val rotation: Vector2f
        get() = Vector2f(adaptee.rotation.x, adaptee.rotation.y)
    override val entity: Entity
        get() = adaptee.entityOrThrow.let(Conversions::entity)
    override val entityOrNull: Entity?
        get() = adaptee.entity?.let(Conversions::entity)
    override val player: Player
        get() = adaptee.playerOrThrow.let(Conversions::player)
    override val playerOrNull: Player?
        get() = adaptee.player?.let(Conversions::player)

    override fun sendMessage(message: Component) {
        adaptee.sendMessage(Conversions.Adventure.vanilla(message))
    }
}