package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.ServerScoreboardBridge;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.scoreboard.ScoreboardImpl;
import io.github.pandier.snowball.scoreboard.Scoreboard;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin extends ScoreboardMixin implements ServerScoreboardBridge, SnowballConvertible<Scoreboard> {
    @Shadow public abstract List<Packet<?>> getStartTrackingPackets(Objective objective);
    @Shadow public abstract List<Packet<?>> getStopTrackingPackets(Objective objective);
    @Shadow @Final private Set<Objective> trackedObjectives;

    @Unique private final SnowballAdapterHolder<ScoreboardImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new ScoreboardImpl((ServerScoreboard) (Object) this));
    @Unique protected final Set<ServerGamePacketListenerImpl> snowball$viewers = new HashSet<>();

    @Override
    public io.github.pandier.snowball.scoreboard.@NotNull Scoreboard snowball$get() {
        return impl$adapter.get();
    }

    @Override
    public void snowball$addViewer(@NotNull ServerGamePacketListenerImpl viewer) {
        if (!this.snowball$viewers.add(viewer)) return;

        for (PlayerTeam playerTeam : this.getPlayerTeams()) {
            viewer.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true));
        }

        for (Objective objective : this.trackedObjectives) {
            for (Packet<?> packet : this.getStartTrackingPackets(objective)) {
                viewer.send(packet);
            }
        }
    }

    @Override
    public void snowball$removeViewer(@NotNull ServerGamePacketListenerImpl viewer) {
        if (!this.snowball$viewers.remove(viewer)) return;

        for (PlayerTeam playerTeam : this.getPlayerTeams()) {
            viewer.send(ClientboundSetPlayerTeamPacket.createRemovePacket(playerTeam));
        }

        for (Objective objective : this.trackedObjectives) {
            for (Packet<?> packet : this.getStopTrackingPackets(objective)) {
                viewer.send(packet);
            }
        }
    }

    @Override
    public Collection<ServerGamePacketListenerImpl> snowball$getViewers() {
        return this.snowball$viewers;
    }

    @Unique
    public void snowball$broadcast(@NotNull Packet<?> packet) {
        this.snowball$viewers.removeIf(viewer -> {
            if (viewer.getPlayer().hasDisconnected()) return true;
            viewer.send(packet);
            return false;
        });
    }

    @Unique
    public Iterator<ServerPlayer> snowball$getViewersForBroadcast() {
        this.snowball$viewers.removeIf(viewer -> viewer.getPlayer().hasDisconnected());
        return this.snowball$viewers.stream().map(ServerGamePacketListenerImpl::getPlayer).iterator();
    }

    @Redirect(method = "onScoreChanged",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$onScoreChanged$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "onPlayerRemoved",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$onPlayerRemoved$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "onPlayerScoreRemoved",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$onPlayerScoreRemoved$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "setDisplayObjective",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$setDisplayObjective$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "addPlayerToTeam",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$addPlayerToTeam$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "removePlayerFromTeam",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$removePlayerFromTeam$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "onObjectiveChanged",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$onObjectiveChanged$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "onTeamAdded",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$onTeamAdded$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "onTeamChanged",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$onTeamChanged$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "onTeamRemoved",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    public void redirect$onTeamRemoved$broadcast(PlayerList instance, Packet<?> packet) {
        this.snowball$broadcast(packet);
    }

    @Redirect(method = "stopTrackingObjective",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;",
                    ordinal = 0))
    public Iterator<ServerPlayer> redirect$stopTrackingObjective$getViewers(List<?> instance) {
        return this.snowball$getViewersForBroadcast();
    }

    @Redirect(method = "startTrackingObjective",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;",
                    ordinal = 0))
    public Iterator<ServerPlayer> redirect$startTrackingObjective$getViewers(List<?> instance) {
        return this.snowball$getViewersForBroadcast();
    }
}
