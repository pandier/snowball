package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.world.block.BlockStateImpl;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockState.class)
public class BlockStateMixin implements SnowballConvertible<io.github.pandier.snowball.world.block.BlockState> {
    @Unique private final SnowballAdapterHolder<BlockStateImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new BlockStateImpl((BlockState) (Object) this));

    @Override
    public @NotNull io.github.pandier.snowball.world.block.BlockState snowball$get() {
        return impl$adapter.get();
    }
}
