package io.github.pandier.snowball.entity

import io.github.pandier.snowball.math.Location
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.World
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import org.jetbrains.annotations.UnmodifiableView
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
     * Returns the custom name stripped of click events
     * or the [EntityType.name] if a custom name is not set.
     */
    public val name: Component

    /**
     * Returns the entity's [name] formatted using its team.
     */
    public val displayName: Component

    /**
     * The name used for scoreboard scores and teams.
     */
    public val scoreboardName: String

    public val isOnGround: Boolean

    public val isRemoved: Boolean
    public val isAlive: Boolean
    public val isDead: Boolean

    /**
     * An unmodifiable set of the entity's tags.
     */
    public val tags: @UnmodifiableView Set<String>

    /**
     * Adds a new [tag] to the entity.
     *
     * Returns false if the tag already exists or the entity has reached the maximum number of tags.
     */
    public fun addTag(tag: String): Boolean

    /**
     * Removes a [tag] from the entity.
     *
     * Returns false if the tag did not exist.
     */
    public fun removeTag(tag: String): Boolean

    /**
     * Checks if the entity has a [tag].
     */
    public fun hasTag(tag: String): Boolean

    public fun teleport(world: World, location: Location): Boolean

    public fun remove()
}