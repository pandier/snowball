package io.github.pandier.snowball.impl.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.pandier.snowball.Snowball;
import io.github.pandier.snowball.event.entity.EntityDeathEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.bridge.ServerPlayerBridge;
import io.github.pandier.snowball.impl.bridge.ServerScoreboardBridge;
import io.github.pandier.snowball.impl.entity.player.PlayerImpl;
import io.github.pandier.snowball.impl.entity.tracker.EntityDeathTracker;
import net.kyori.adventure.audience.Audience;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin implements ServerPlayerBridge {
    @Shadow @Final private MinecraftServer server;
    @Shadow public ServerGamePacketListenerImpl connection;
    @Shadow public abstract ServerLevel level();

    @Unique private Component snowball$joinMessage = null;
    @Unique @Nullable private ServerScoreboard snowball$scoreboard = null;
    @Unique private boolean snowball$isScoreboardInitialized = false;

    @Override
    public PlayerImpl snowball$createAdapter() {
        return new PlayerImpl((ServerPlayer) (Object) this);
    }

    @Override
    public void snowball$setJoinMessage(Component message) {
        this.snowball$joinMessage = message;
    }

    @Override
    public Component snowball$getJoinMessage() {
        return this.snowball$joinMessage;
    }

    @Override
    public void snowball$setScoreboard(@Nullable ServerScoreboard scoreboard) {
        if (this.snowball$isScoreboardInitialized) {
            ((ServerScoreboardBridge) this.snowball$getScoreboard()).snowball$removeViewer((ServerPlayer) (Object) this);
        }
        this.snowball$scoreboard = scoreboard != this.server.getScoreboard() ? scoreboard : null;
        if (this.snowball$isScoreboardInitialized) {
            ((ServerScoreboardBridge) this.snowball$getScoreboard()).snowball$addViewer((ServerPlayer) (Object) this);
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
        ((ServerScoreboardBridge) this.snowball$getScoreboard()).snowball$addViewer((ServerPlayer) (Object) this);
    }

    @ModifyVariable(method = "die", at = @At(value = "STORE", ordinal = 0))
    public boolean inject$die$playerStartDeathTracker(boolean showDeathMessages) {
        EntityDeathTracker tracker = this.snowball$startDeathTracker();
        if (tracker == null)
            return showDeathMessages;
        return false;
    }

    @WrapOperation(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
    public void inject$die$cancelCombatKillPacket(ServerGamePacketListenerImpl instance, Packet<?> packet, Operation<Void> original) {
        if (this.snowball$deathTracker == null) {
            original.call(instance, packet);
        }
    }

    @Inject(method = "die",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    public void inject$die$playerDeathEvent(DamageSource damageSource, CallbackInfo ci) {
        if (this.snowball$deathTracker != null && this.level().getGameRules().get(GameRules.SHOW_DEATH_MESSAGES)) {
            Team team = this.getTeam();
            if (team == null || team.getDeathMessageVisibility() == Team.Visibility.ALWAYS) {
                this.snowball$deathTracker.setAudience(Snowball.getServer());
            } else if (team.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
                ServerPlayer self = (ServerPlayer) (Object) this;
                Audience audience = Audience.audience(this.server.getPlayerList().getPlayers().stream()
                        .filter(player -> player.getTeam() == team && player != self)
                        .map(Conversions.INSTANCE::snowball)
                        .toList());
                this.snowball$deathTracker.setAudience(audience);
            } else if (team.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
                Audience audience = Audience.audience(this.server.getPlayerList().getPlayers().stream()
                        .filter(player -> player.getTeam() != team)
                        .map(Conversions.INSTANCE::snowball)
                        .toList());
                this.snowball$deathTracker.setAudience(audience);
            }
        }

        EntityDeathEvent event = snowball$die(damageSource);
        if (event != null) {
            Component message = event.getMessage() == null ? CommonComponents.EMPTY : Conversions.Adventure.INSTANCE.vanilla(event.getMessage());
            this.connection.send(new ClientboundPlayerCombatKillPacket(this.getId(), message));
        }
    }

    @Inject(method = "disconnect", at = @At("TAIL"))
    public void inject$disconnet(CallbackInfo ci) {
        ((ServerScoreboardBridge) this.snowball$getScoreboard()).snowball$removeViewer((ServerPlayer) (Object) this);
        ((PlayerImpl) snowball$get()).disconnect();
    }
}
