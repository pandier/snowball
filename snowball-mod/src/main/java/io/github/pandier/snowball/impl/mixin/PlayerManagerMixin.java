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
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow @Final private static Logger LOGGER;

    @Redirect(method = "onPlayerConnect",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getWorld(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/server/world/ServerWorld;"))
    private ServerWorld redirect$onPlayerConnect$prepareEvent(MinecraftServer server, RegistryKey<net.minecraft.world.World> key, ClientConnection connection, ServerPlayerEntity vPlayer) {
        ServerWorld vWorld = server.getWorld(key);
        if (vWorld == null) {
            LOGGER.warn("Player '{}' tried to connect with an unknown dimension {}, defaulting to overworld", vPlayer.getGameProfile().getName(), key);
            vWorld = server.getOverworld();
        }

        final Player player = Conversions.INSTANCE.snowball(vPlayer);
        final World originalWorld = Conversions.INSTANCE.snowball(vWorld);
        final Location originalLocation = player.getLocation();

        final PlayerPrepareEvent event = new PlayerPrepareEvent(player.getGameProfile(), originalWorld, player.getLocation());
        SnowballImpl.INSTANCE.getEventManager().dispatch(event);

        final Location location = event.getLocation();
        final World world = event.getWorld();

        if (!originalLocation.equals(location) || !originalWorld.equals(world)) {
            vPlayer.refreshPositionAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        return ((WorldImpl) world).getAdaptee();
    }

    @Redirect(method = "onPlayerConnect",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"))
    private void redirect$onPlayerConnect$cacheJoinMessage(PlayerManager playerManager, Text message, boolean overlay, ClientConnection connection, ServerPlayerEntity vPlayer) {
        // Cache the join message so we can use it later in the join event
        ((ServerPlayerEntityBridge) vPlayer).snowball$setJoinMessage(message);
    }
}
