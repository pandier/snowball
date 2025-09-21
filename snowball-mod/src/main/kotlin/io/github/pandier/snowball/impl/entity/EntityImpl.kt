package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.math.Location
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.World
import net.minecraft.server.world.ServerWorld
import java.util.UUID

open class EntityImpl(
    override val adaptee: net.minecraft.entity.Entity
) : SnowballAdapter(adaptee), Entity {
    override val id: Int
        get() = adaptee.id
    override val uuid: UUID
        get() = adaptee.uuid

    override val world: World
        get() = Conversions.snowball(adaptee.entityWorld as ServerWorld)

    override var position: Vector3d
        get() = Vector3d(adaptee.x, adaptee.y, adaptee.z)
        set(value) = adaptee.requestTeleport(value.x, value.y, value.z)
    override var rotation: Vector2f
        get() = Vector2f(adaptee.yaw, adaptee.pitch)
        set(value) = adaptee.rotate(value.x, false, value.y, false)
    override var location: Location
        get() = Location(adaptee.x, adaptee.y, adaptee.z, adaptee.yaw, adaptee.pitch)
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
}