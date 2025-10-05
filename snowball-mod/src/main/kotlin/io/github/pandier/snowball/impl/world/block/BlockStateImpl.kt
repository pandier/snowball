package io.github.pandier.snowball.impl.world.block

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.world.block.BlockState
import io.github.pandier.snowball.world.block.BlockType

class BlockStateImpl(
    override val adaptee: net.minecraft.world.level.block.state.BlockState,
) : SnowballAdapter(adaptee), BlockState {
    override val type: BlockType
        get() = Conversions.snowball(adaptee.block)
}
