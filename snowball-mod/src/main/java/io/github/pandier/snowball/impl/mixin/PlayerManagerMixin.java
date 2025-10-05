package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.entity.Player;
import io.github.pandier.snowball.event.player.PlayerPrepareEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.SnowballImpl;
import io.github.pandier.snowball.impl.bridge.ServerPlayerEntityBridge;
import io.github.pandier.snowball.impl.world.WorldImpl;
import io.github.pandier.snowball.math.Location;
import io.github.pandier.snowball.world.World;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "onPlayerConnect",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;getEntityWorld()Lnet/minecraft/server/world/ServerWorld;"))
    private void redirect$onPlayerConnect$prepareEvent(ClientConnection connection, ServerPlayerEntity vPlayer, ConnectedClientData clientData, CallbackInfo ci) {
        final Player player = Conversions.INSTANCE.snowball(vPlayer);
        final World originalWorld = Conversions.INSTANCE.snowball(vPlayer.getEntityWorld());
        final Location originalLocation = player.getLocation();

        final PlayerPrepareEvent event = new PlayerPrepareEvent(player.getGameProfile(), originalWorld, player.getLocation());
        SnowballImpl.INSTANCE.getEventManager().dispatch(event);

        final Location location = event.getLocation();
        final World world = event.getWorld();

        if (!originalLocation.equals(location) || !originalWorld.equals(world)) {
            vPlayer.refreshPositionAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        vPlayer.setServerWorld(((WorldImpl) world).getAdaptee());
    }

    @Redirect(method = "onPlayerConnect",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"))
    private void redirect$onPlayerConnect$cacheJoinMessage(PlayerManager playerManager, Text message, boolean overlay, ClientConnection connection, ServerPlayerEntity vPlayer) {
        // Cache the join message so we can use it later in the join event
        ((ServerPlayerEntityBridge) vPlayer).snowball$setJoinMessage(message);
    }
}
