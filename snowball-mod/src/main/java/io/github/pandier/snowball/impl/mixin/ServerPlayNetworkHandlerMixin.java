package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.entity.Player;
import io.github.pandier.snowball.event.player.PlayerLeaveEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.SnowballImpl;
import net.kyori.adventure.text.Component;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Redirect(method = "cleanUp",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"))
    private void redirect$cleanUp$leaveEvent(PlayerManager playerManager, Text vMessage, boolean overlay) {
        final Player player = Conversions.INSTANCE.snowball(this.player);
        final Component originalMessage = Conversions.Adventure.INSTANCE.adventure(vMessage);

        final PlayerLeaveEvent event = new PlayerLeaveEvent(player, originalMessage, SnowballImpl.INSTANCE.getServer());
        SnowballImpl.INSTANCE.getEventManager().notify(event);

        final Component message = event.getMessage();
        if (message != null) {
            event.getAudience().sendMessage(message);
        }
    }
}
