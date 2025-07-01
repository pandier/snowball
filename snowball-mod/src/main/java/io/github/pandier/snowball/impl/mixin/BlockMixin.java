package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.world.block.BlockTypeImpl;
import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Block.class)
public class BlockMixin implements SnowballConvertible<io.github.pandier.snowball.world.block.BlockType> {
    @Unique private final SnowballAdapterHolder<BlockTypeImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new BlockTypeImpl((Block) (Object) this));

    @Override
    public @NotNull io.github.pandier.snowball.world.block.BlockType snowball$get() {
        return impl$adapter.get();
    }
}
