package io.github.pandier.snowball.impl.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntityMixin {

    @ModifyVariable(
            method = "actuallyHurt",
            at = @At("LOAD"),
            argsOnly = true,
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/Identifier;I)V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V")))
    public float inject$actuallyHurt$damageEventPlayer(float value, ServerLevel level, DamageSource damageSource) {
        return snowball$callDamageEvent(value, damageSource);
    }
}
