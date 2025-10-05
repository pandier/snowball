package io.github.pandier.snowball.world

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.math.Vector3i
import io.github.pandier.snowball.world.block.BlockState
import net.kyori.adventure.key.Keyed
import java.util.UUID

public interface World : Keyed {
    public val entities: Iterable<Entity>

    public fun getEntity(uuid: UUID): Entity?
    public fun getEntity(id: Int): Entity?

    public fun getBlockState(pos: Vector3i): BlockState {
        return getBlockState(pos.x, pos.y, pos.z)
    }

    public fun getBlockState(x: Int, y: Int, z: Int): BlockState

    public fun setBlockState(pos: Vector3i, state: BlockState) {
        setBlockState(pos.x, pos.y, pos.z, state)
    }

    public fun setBlockState(x: Int, y: Int, z: Int, state: BlockState)
}
