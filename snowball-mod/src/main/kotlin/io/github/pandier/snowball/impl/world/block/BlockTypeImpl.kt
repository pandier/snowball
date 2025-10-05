package io.github.pandier.snowball.impl.world.block

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.world.block.BlockState
import io.github.pandier.snowball.world.block.BlockType
import net.minecraft.world.level.block.Block

class BlockTypeImpl(
    override val adaptee: Block
) : SnowballAdapter(adaptee), BlockType {
    override val blockState: BlockState
        get() = Conversions.snowball(adaptee.defaultBlockState())
}