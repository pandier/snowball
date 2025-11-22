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
import java.util.function.Consumer;

@Mixin(ServerScoreboard.class)
public abstract class ServerScoreboardMixin extends ScoreboardMixin implements ServerScoreboardBridge, SnowballConvertible<Scoreboard> {
    @Shadow public abstract List<Packet<?>> getStartTrackingPackets(Objective objective);
    @Shadow public abstract List<Packet<?>> getStopTrackingPackets(Objective objective);
    @Shadow @Final private Set<Objective> trackedObjectives;

    @Unique private final SnowballAdapterHolder<ScoreboardImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new ScoreboardImpl((ServerScoreboard) (Object) this));
    @Unique protected final Set<ServerPlayer> snowball$viewers = new HashSet<>();

    @Override
    public io.github.pandier.snowball.scoreboard.@NotNull Scoreboard snowball$get() {
        return impl$adapter.get();
    }

    @Override
    public void snowball$addViewer(@NotNull ServerPlayer viewer) {
        if (!this.snowball$viewers.add(viewer)) return;

        for (PlayerTeam playerTeam : this.getPlayerTeams()) {
            viewer.connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true));
        }

        for (Objective objective : this.trackedObjectives) {
            for (Packet<?> packet : this.getStartTrackingPackets(objective)) {
                viewer.connection.send(packet);
            }
        }
    }

    @Override
    public void snowball$removeViewer(@NotNull ServerPlayer viewer) {
        if (!this.snowball$viewers.remove(viewer)) return;

        for (PlayerTeam playerTeam : this.getPlayerTeams()) {
            viewer.connection.send(ClientboundSetPlayerTeamPacket.createRemovePacket(playerTeam));
        }

        for (Objective objective : this.trackedObjectives) {
            for (Packet<?> packet : this.getStopTrackingPackets(objective)) {
                viewer.connection.send(packet);
            }
        }
    }

    @Override
    public Collection<ServerPlayer> snowball$getViewers() {
        return this.snowball$viewers;
    }

    @Unique
    public void snowball$forEachViewer(@NotNull Consumer<ServerPlayer> consumer) {
        this.snowball$viewers.removeIf(viewer -> {
            if (viewer.hasDisconnected()) return true;
            consumer.accept(viewer);
            return false;
        });
    }

    @Unique
    public void snowball$broadcast(@NotNull Packet<?> packet) {
        this.snowball$forEachViewer(viewer -> viewer.connection.send(packet));
    }

    @Unique
    public List<ServerPlayer> snowball$getViewersForBroadcast() {
        // TODO: This could probably be optimized so that we're not copying the list.
        //       Perhaps a stripped down version of List?
        List<ServerPlayer> list = new ArrayList<>(this.snowball$viewers.size());
        this.snowball$forEachViewer(list::add);
        return list;
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
                    target = "Lnet/minecraft/server/players/PlayerList;getPlayers()Ljava/util/List;"))
    public List<ServerPlayer> redirect$stopTrackingObjective$getViewers(PlayerList instance) {
        return this.snowball$getViewersForBroadcast();
    }

    @Redirect(method = "startTrackingObjective",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;getPlayers()Ljava/util/List;"))
    public List<ServerPlayer> redirect$startTrackingObjective$getViewers(PlayerList instance) {
        return this.snowball$getViewersForBroadcast();
    }
}
