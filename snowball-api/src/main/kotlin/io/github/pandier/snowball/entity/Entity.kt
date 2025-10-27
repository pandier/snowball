package io.github.pandier.snowball.entity

import io.github.pandier.snowball.math.Location
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.World
import net.kyori.adventure.sound.Sound
import java.util.UUID

public interface Entity : Sound.Emitter {
    public val id: Int
    public val uuid: UUID

    public val world: World

    public var position: Vector3d
    public var rotation: Vector2f
    public var location: Location

    public val isRemoved: Boolean
    public val isAlive: Boolean
    public val isDead: Boolean

    public fun teleport(world: World, location: Location): Boolean

    public fun remove()
}