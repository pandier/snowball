package io.github.pandier.snowball.entity

import io.github.pandier.snowball.math.Location
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.World
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import java.util.UUID

public interface Entity : Sound.Emitter {
    public val id: Int
    public val uuid: UUID
    public val type: EntityType<*>

    public val world: World

    public var position: Vector3d
    public var rotation: Vector2f
    public var location: Location

    public var velocity: Vector3d

    public var hasGravity: Boolean
    public var isInvulnerable: Boolean
    public var isGlowing: Boolean

    /**
     * Specifies if the entity's name should always be displayed above it.
     */
    public var isCustomNameVisible: Boolean

    /**
     * A custom name for the entity.
     */
    public var customName: Component?

    /**
     * Returns [EntityType.name] or the custom name stripped of click events.
     */
    public val name: Component

    /**
     * Returns the entity's [name] formatted using its team.
     */
    public val formattedName: Component

    /**
     * The name used for scoreboard scores and teams.
     */
    public val scoreboardName: String

    public val isRemoved: Boolean
    public val isAlive: Boolean
    public val isDead: Boolean

    public fun teleport(world: World, location: Location): Boolean

    public fun remove()
}