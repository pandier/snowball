package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.EntityType
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.world.WorldImpl
import io.github.pandier.snowball.math.Location
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.World
import net.kyori.adventure.text.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import org.jetbrains.annotations.ApiStatus
import java.util.UUID

open class EntityImpl(
    adaptee: net.minecraft.world.entity.Entity
) : SnowballAdapter(), Entity {
    protected var adapteeInternal: net.minecraft.world.entity.Entity = adaptee

    override val adaptee: net.minecraft.world.entity.Entity
        get() = adapteeInternal

    override val id: Int
        get() = adaptee.id
    override val uuid: UUID
        get() = adaptee.uuid
    override val type: EntityType<*>
        get() = Conversions.snowball(adaptee.type)

    override val world: World
        get() = Conversions.snowball(adaptee.level() as ServerLevel)

    override var position: Vector3d
        get() = Vector3d(adaptee.x, adaptee.y, adaptee.z)
        set(value) = adaptee.teleportTo(value.x, value.y, value.z)
    override var rotation: Vector2f
        get() = Vector2f(adaptee.xRot, adaptee.yRot)
        set(value) = adaptee.forceSetRotation(value.x, false, value.y, false)
    override var location: Location
        get() = Location(adaptee.x, adaptee.y, adaptee.z, adaptee.xRot, adaptee.yRot)
        set(value) {
            position = value.position
            rotation = value.rotation
        }

    override var velocity: Vector3d
        get() = adaptee.deltaMovement.let { Vector3d(it.x, it.y, it.z) }
        set(value) { adaptee.deltaMovement = Vec3(value.x, value.y, value.z) }

    override var hasGravity: Boolean
        get() = !adaptee.isNoGravity
        set(value) { adaptee.isNoGravity = !value }

    override var isInvulnerable: Boolean
        get() = adaptee.isInvulnerable
        set(value) { adaptee.isInvulnerable = value }

    override var isGlowing: Boolean
        get() = adaptee.hasGlowingTag()
        set(value) { adaptee.setGlowingTag(value) }

    override var isCustomNameVisible: Boolean
        get() = adaptee.isCustomNameVisible
        set(value) { adaptee.isCustomNameVisible = value }

    override var customName: Component?
        get() = adaptee.customName?.let(Conversions.Adventure::adventure)
        set(value) { adaptee.customName = value?.let(Conversions.Adventure::vanilla) }

    override val name: Component
        get() = adaptee.name.let(Conversions.Adventure::adventure)

    override val displayName: Component
        get() = adaptee.displayName.let(Conversions.Adventure::adventure)

    override val scoreboardName: String
        get() = adaptee.scoreboardName

    override val isRemoved: Boolean
        get() = adaptee.isRemoved
    override val isAlive: Boolean
        get() = adaptee.isAlive
    override val isDead: Boolean
        get() = !isAlive

    override fun teleport(world: World, location: Location): Boolean {
        return adaptee.teleportTo((world as WorldImpl).adaptee,
            location.position.x, location.position.y, location.position.z,
            setOf(), location.yaw, location.pitch, false)
    }

    override fun remove() {
        adaptee.remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED)
    }

    /**
     * Updates the adaptee of this entity adapter.
     * Called when entities teleport across dimensions because the entity instance changes.
     */
    @ApiStatus.Internal
    fun updateAdapatee(newAdaptee: net.minecraft.world.entity.Entity) {
        if (newAdaptee.type != adapteeInternal.type)
            throw IllegalArgumentException("New entity adaptee is not of the same type")
        adapteeInternal = newAdaptee
    }
}