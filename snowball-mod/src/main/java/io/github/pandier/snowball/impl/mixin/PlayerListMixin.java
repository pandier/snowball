package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.Snowball;
import io.github.pandier.snowball.entity.player.Player;
import io.github.pandier.snowball.event.player.PlayerPrepareEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.bridge.ServerPlayerBridge;
import io.github.pandier.snowball.impl.world.WorldImpl;
import io.github.pandier.snowball.math.Location;
import io.github.pandier.snowball.world.World;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "placeNewPlayer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;level()Lnet/minecraft/server/level/ServerLevel;"))
    private void redirect$onPlayerConnect$prepareEvent(Connection connection, ServerPlayer vPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
        final Player player = Conversions.INSTANCE.snowball(vPlayer);
        final World originalWorld = Conversions.INSTANCE.snowball(vPlayer.level());
        final Location originalLocation = player.getLocation();

        final PlayerPrepareEvent event = new PlayerPrepareEvent(player.getGameProfile(), originalWorld, player.getLocation());
        Snowball.getEventManager().dispatch(event);

        final Location location = event.getLocation();
        final World world = event.getWorld();

        if (!originalLocation.equals(location) || !originalWorld.equals(world)) {
            vPlayer.snapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        vPlayer.setServerLevel(((WorldImpl) world).getAdaptee());
    }

    @Redirect(method = "placeNewPlayer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void redirect$onPlayerConnect$cacheJoinMessage(PlayerList playerList, Component message, boolean overlay, Connection connection, ServerPlayer vPlayer) {
        // Cache the join message so we can use it later in the join event
        ((ServerPlayerBridge) vPlayer).snowball$setJoinMessage(message);
    }
}
