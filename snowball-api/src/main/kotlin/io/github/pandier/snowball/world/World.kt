package io.github.pandier.snowball.world

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.EntityType
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.math.Vector3i
import io.github.pandier.snowball.world.block.BlockState
import net.kyori.adventure.key.Key
import java.util.UUID
import java.util.function.Supplier

public interface World {
    public val key: Key

    public val entities: Iterable<Entity>

    public fun getEntity(uuid: UUID): Entity?

    public fun getEntity(id: Int): Entity?

    public fun <T : Entity> createEntity(type: EntityType<T>, position: Vector3d): T

    public fun <T : Entity> createEntity(type: Supplier<EntityType<T>>, position: Vector3d): T {
        return createEntity(type.get(), position)
    }

    public fun spawnEntity(entity: Entity): Boolean

    public fun getBlockState(pos: Vector3i): BlockState {
        return getBlockState(pos.x, pos.y, pos.z)
    }

    public fun getBlockState(x: Int, y: Int, z: Int): BlockState

    public fun setBlockState(pos: Vector3i, state: BlockState) {
        setBlockState(pos.x, pos.y, pos.z, state)
    }

    public fun setBlockState(x: Int, y: Int, z: Int, state: BlockState)
}
