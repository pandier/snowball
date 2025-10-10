package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.ServerPlayerBridge;
import io.github.pandier.snowball.impl.entity.player.PlayerImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin implements ServerPlayerBridge {
    @Unique private Component impl$joinMessage = null;

    @Override
    public PlayerImpl snowball$createAdapter() {
        return new PlayerImpl((ServerPlayer) (Object) this);
    }

    @Override
    public void snowball$setJoinMessage(Component message) {
        this.impl$joinMessage = message;
    }

    @Override
    public Component snowball$getJoinMessage() {
        return this.impl$joinMessage;
    }
}
