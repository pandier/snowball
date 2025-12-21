package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.Snowball;
import io.github.pandier.snowball.entity.player.Player;
import io.github.pandier.snowball.event.entity.player.PlayerLeaveEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.SnowballImpl;
import io.github.pandier.snowball.impl.bridge.ServerGamePacketListenerImplBridge;
import io.github.pandier.snowball.impl.bridge.ServerScoreboardBridge;
import net.kyori.adventure.text.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin extends ServerCommonPacketListenerImplMixin implements ServerGamePacketListenerImplBridge {
    @Shadow public ServerPlayer player;

    @Unique @Nullable private ServerScoreboard snowball$scoreboard = null;
    @Unique private boolean snowball$isScoreboardInitialized = false;

    @Override
    public void snowball$setScoreboard(@Nullable ServerScoreboard scoreboard) {
        if (this.snowball$isScoreboardInitialized) {
            ((ServerScoreboardBridge) this.snowball$getScoreboard()).snowball$removeViewer((ServerGamePacketListenerImpl) (Object) this);
        }
        this.snowball$scoreboard = scoreboard != this.server.getScoreboard() ? scoreboard : null;
        if (this.snowball$isScoreboardInitialized) {
            ((ServerScoreboardBridge) this.snowball$getScoreboard()).snowball$addViewer((ServerGamePacketListenerImpl) (Object) this);
        }
    }

    @Override
    public @NotNull ServerScoreboard snowball$getScoreboard() {
        return this.snowball$scoreboard != null ? this.snowball$scoreboard : this.server.getScoreboard();
    }

    @Override
    public void snowball$initializeScoreboard() {
        if (this.snowball$isScoreboardInitialized) return;
        this.snowball$isScoreboardInitialized = true;
        ((ServerScoreboardBridge) this.snowball$getScoreboard()).snowball$addViewer((ServerGamePacketListenerImpl) (Object) this);
    }

    @Inject(method = "removePlayerFromWorld",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;disconnect()V"))
    private void inject$removePlayerFromWorld(CallbackInfo info) {
        ((ServerScoreboardBridge) this.snowball$getScoreboard()).snowball$removeViewer((ServerGamePacketListenerImpl) (Object) this);
    }

    @Redirect(method = "removePlayerFromWorld",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void redirect$cleanUp$leaveEvent(PlayerList playerList, net.minecraft.network.chat.Component vMessage, boolean overlay) {
        final Player player = Conversions.INSTANCE.snowball(this.player);
        final Component originalMessage = Conversions.Adventure.INSTANCE.adventure(vMessage);

        final PlayerLeaveEvent event = new PlayerLeaveEvent(player, originalMessage, SnowballImpl.INSTANCE.getServer());
        Snowball.getEventManager().dispatch(event);

        final Component message = event.getMessage();
        if (message != null) {
            event.getAudience().sendMessage(message);
        }
    }
}
