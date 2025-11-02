package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.Snowball;
import io.github.pandier.snowball.event.entity.EntityDamageEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.entity.LivingEntityImpl;
import io.github.pandier.snowball.impl.event.entity.EntityDamageEventImpl;
import io.github.pandier.snowball.impl.event.entity.EntityDeathEventImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Unique private static final ThreadLocal<Float> DAMAGE = ThreadLocal.withInitial(() -> 0f);
    @Unique private static final ThreadLocal<Float> BASE_DAMAGE = ThreadLocal.withInitial(() -> 0f);
    @Unique private static final ThreadLocal<Boolean> DAMAGE_PREVENTED = ThreadLocal.withInitial(() -> false);

    @Shadow protected float lastHurt;

    @Override
    public LivingEntityImpl snowball$createAdapter() {
        return new LivingEntityImpl((LivingEntity) (Object) this);
    }

    @Inject(method = "hurtServer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getUseItem()Lnet/minecraft/world/item/ItemStack;"))
    public void inject$hurtServer$initBaseDamage(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        BASE_DAMAGE.set(f);
    }

    @ModifyVariable(
            method = "actuallyHurt",
            at = @At("LOAD"),
            argsOnly = true,
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/ResourceLocation;I)V"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getCombatTracker()Lnet/minecraft/world/damagesource/CombatTracker;")))
    public float modify$actuallyHurt$damageEvent(float value, ServerLevel level, DamageSource damageSource) {
        return snowball$callDamageEvent(value, damageSource);
    }

    @Unique
    protected float snowball$callDamageEvent(float amount, DamageSource vSource) {
        io.github.pandier.snowball.entity.LivingEntity entity = Conversions.INSTANCE.snowball((LivingEntity) (Object) this);
        io.github.pandier.snowball.entity.damage.DamageSource source = Conversions.INSTANCE.snowball(vSource);

        float baseAmount = BASE_DAMAGE.get();

        EntityDamageEvent.Allow allow = new EntityDamageEventImpl.Allow(entity, source, baseAmount, amount);
        Snowball.getEventManager().dispatch(allow);

        if (!allow.getAllowed()) {
            EntityDamageEvent.Canceled canceled = new EntityDamageEventImpl.Canceled(entity, source, baseAmount, amount);
            Snowball.getEventManager().dispatch(canceled);

            DAMAGE_PREVENTED.set(true);
            return 0f;
        }

        EntityDamageEvent.Action action = new EntityDamageEventImpl.Action(entity, source, baseAmount, amount);
        Snowball.getEventManager().dispatch(action);

        if (action.isDefaultPrevented()) {
            DAMAGE_PREVENTED.set(true);
            return 0f;
        }

        amount = action.getAmount();

        if (Float.isNaN(amount) || Float.isInfinite(amount)) {
            amount = Float.MAX_VALUE;
        }

        // Reset the base damage in case more damage was dealt during the events
        BASE_DAMAGE.set(baseAmount);
        DAMAGE.set(amount);

        return amount;
    }

    @Inject(method = "hurtServer",
            cancellable = true,
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V",
                    shift = At.Shift.AFTER))
    public void inject$hurtServer$preventByEvent(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (DAMAGE_PREVENTED.get()) {
            cir.setReturnValue(false);
        }
        DAMAGE_PREVENTED.remove();
    }

    @Redirect(
            method = "hurtServer",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;lastHurt:F", ordinal = 1),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 1)))
    public void redirect$actuallyHurt$deferLastHurt(LivingEntity instance, float value) {
        // Do nothing, because we'll do it ourselves a bit later (inject$hurtServer$preventByEvent)
    }

    @Redirect(
            method = "hurtServer",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;invulnerableTime:I"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 1)))
    public void redirect$actuallyHurt$deferInvulnerableTime(LivingEntity instance, int value) {
        // Do nothing, because we'll do it ourselves a bit later (inject$hurtServer$preventByEvent)
    }

    @Inject(method = "hurtServer",
            order = 900,
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER))
    public void inject$hurtServer$setFieldsAfterEvent(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        this.lastHurt = f;
        this.invulnerableTime = 20;
    }

    @Inject(method = "hurtServer", at = @At("TAIL"))
    public void inject$hurtServer$entityDamageEventPost(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        EntityDamageEvent.Post event = new EntityDamageEventImpl.Post(
                Conversions.INSTANCE.snowball((LivingEntity) (Object) this),
                Conversions.INSTANCE.snowball(damageSource),
                BASE_DAMAGE.get(),
                DAMAGE.get()
        );

        BASE_DAMAGE.remove();
        DAMAGE.remove();

        Snowball.getEventManager().dispatch(event);
    }
}
