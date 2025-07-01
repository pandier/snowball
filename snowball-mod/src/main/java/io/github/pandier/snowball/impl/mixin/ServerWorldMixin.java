package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.world.WorldImpl;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements SnowballConvertible<WorldImpl> {
    @Unique private final SnowballAdapterHolder<WorldImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new WorldImpl((ServerWorld) (Object) this));

    @Override
    public @NotNull WorldImpl snowball$get() {
        return impl$adapter.get();
    }
}
