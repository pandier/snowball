package io.github.pandier.snowball.impl.world

import com.google.common.collect.Iterables
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.world.block.BlockStateImpl
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockState
import net.kyori.adventure.key.Key
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import java.util.UUID

class WorldImpl(
    override val adaptee: ServerLevel
) : SnowballAdapter(adaptee), World {
    override val entities: Iterable<Entity>
        get() = Iterables.transform(adaptee.allEntities, Conversions::snowball)

    override fun getEntity(uuid: UUID): Entity? {
        return adaptee.getEntity(uuid)?.let(Conversions::snowball)
    }

    override fun getEntity(id: Int): Entity? {
        return adaptee.getEntity(id)?.let(Conversions::snowball)
    }

    override fun getBlockState(x: Int, y: Int, z: Int): BlockState {
        return Conversions.snowball(adaptee.getBlockState(BlockPos(x, y, z)))
    }

    override fun setBlockState(x: Int, y: Int, z: Int, state: BlockState) {
        adaptee.setBlockAndUpdate(BlockPos(x, y, z), (state as BlockStateImpl).adaptee)
    }

    override fun key(): Key {
        return Conversions.Adventure.adventure(adaptee.dimension().location())
    }
}