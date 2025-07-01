package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends EntityMixin {

    @Override
    public PlayerImpl snowball$createAdapter() {
        return new PlayerImpl((ServerPlayerEntity) (Object) this);
    }
}
