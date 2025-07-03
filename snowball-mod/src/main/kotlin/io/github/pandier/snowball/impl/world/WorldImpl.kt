package io.github.pandier.snowball.impl.world

import com.google.common.collect.Iterables
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.world.block.BlockStateImpl
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockChangeFlag
import io.github.pandier.snowball.world.block.BlockState
import net.kyori.adventure.key.Key
import net.minecraft.block.Block
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import java.util.EnumSet
import java.util.UUID

class WorldImpl(
    override val adaptee: ServerWorld
) : SnowballAdapter(adaptee), World {
    override val entities: Iterable<Entity>
        get() = Iterables.transform(adaptee.iterateEntities(), Conversions::snowball)

    override fun getEntity(uuid: UUID): Entity? {
        return adaptee.getEntity(uuid)?.let(Conversions::snowball)
    }

    override fun getEntity(id: Int): Entity? {
        return adaptee.getEntityById(id)?.let(Conversions::snowball)
    }

    override fun getBlockState(x: Int, y: Int, z: Int): BlockState {
        return Conversions.snowball(adaptee.getBlockState(BlockPos(x, y, z)))
    }

    override fun setBlockState(x: Int, y: Int, z: Int, state: BlockState) {
        adaptee.setBlockState(BlockPos(x, y, z), (state as BlockStateImpl).adaptee)
    }

    override fun setBlockState(x: Int, y: Int, z: Int, state: BlockState, flags: EnumSet<BlockChangeFlag>) {
        adaptee.setBlockState(BlockPos(x, y, z), (state as BlockStateImpl).adaptee, blockChangeFlags(flags))
    }

    private fun blockChangeFlags(flags: EnumSet<BlockChangeFlag>): Int {
        var mask = 0
        for (flag in flags) {
            mask = mask or when (flag) {
                BlockChangeFlag.NOTIFY_NEIGHBORS -> Block.NOTIFY_NEIGHBORS
                BlockChangeFlag.NOTIFY_PLAYERS -> Block.NOTIFY_LISTENERS
                BlockChangeFlag.SKIP_DROPS -> Block.SKIP_DROPS
            }
        }
        return mask
    }

    override fun key(): Key {
        return Conversions.Adventure.adventure(adaptee.registryKey.value)
    }
}