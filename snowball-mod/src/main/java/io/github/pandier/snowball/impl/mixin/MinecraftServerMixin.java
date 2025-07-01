package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.server.ServerImpl;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements SnowballConvertible<ServerImpl> {
    @Unique private final ServerImpl impl$adapter = new ServerImpl((MinecraftServer) (Object) this);

    @Override
    public @NotNull ServerImpl snowball$get() {
        return impl$adapter;
    }
}
