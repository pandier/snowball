package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.entity.Player;
import io.github.pandier.snowball.event.player.PlayerLeaveEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.SnowballImpl;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @Redirect(method = "removePlayerFromWorld",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void redirect$cleanUp$leaveEvent(PlayerList playerList, net.minecraft.network.chat.Component vMessage, boolean overlay) {
        final Player player = Conversions.INSTANCE.snowball(this.player);
        final Component originalMessage = Conversions.Adventure.INSTANCE.adventure(vMessage);

        final PlayerLeaveEvent event = new PlayerLeaveEvent(player, originalMessage, SnowballImpl.INSTANCE.getServer());
        SnowballImpl.INSTANCE.getEventManager().dispatch(event);

        final Component message = event.getMessage();
        if (message != null) {
            event.getAudience().sendMessage(message);
        }
    }
}
