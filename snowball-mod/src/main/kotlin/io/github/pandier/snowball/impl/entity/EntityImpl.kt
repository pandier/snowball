package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.world.WorldImpl
import io.github.pandier.snowball.math.Location
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.World
import net.minecraft.server.level.ServerLevel
import java.util.UUID

open class EntityImpl(
    adaptee: net.minecraft.world.entity.Entity
) : SnowballAdapter(adaptee), Entity {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: net.minecraft.world.entity.Entity = adaptee

    override val id: Int
        get() = adaptee.id
    override val uuid: UUID
        get() = adaptee.uuid

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
}