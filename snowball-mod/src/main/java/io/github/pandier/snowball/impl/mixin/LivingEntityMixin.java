package io.github.pandier.snowball.impl.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.pandier.snowball.Snowball;
import io.github.pandier.snowball.event.entity.EntityDamageEvent;
import io.github.pandier.snowball.event.entity.EntityDeathEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.entity.ItemEntityImpl;
import io.github.pandier.snowball.impl.entity.LivingEntityImpl;
import io.github.pandier.snowball.impl.entity.tracker.EntityDeathTracker;
import io.github.pandier.snowball.impl.event.entity.EntityDamageEventImpl;
import io.github.pandier.snowball.impl.event.entity.EntityDeathEventImpl;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Unique private static final ThreadLocal<Float> DAMAGE = ThreadLocal.withInitial(() -> 0f);
    @Unique private static final ThreadLocal<Float> BASE_DAMAGE = ThreadLocal.withInitial(() -> 0f);
    @Unique private static final ThreadLocal<Boolean> DAMAGE_PREVENTED = ThreadLocal.withInitial(() -> false);

    @Shadow protected float lastHurt;
    @Shadow public abstract CombatTracker getCombatTracker();

    @Unique protected EntityDeathTracker snowball$deathTracker;

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

    @Inject(method = "die",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;"))
    public void inject$die$startDeathTracker(DamageSource damageSource, CallbackInfo ci) {
        this.snowball$startDeathTracker();
    }

    @Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    public void inject$die$entityDeathEvent(DamageSource damageSource, CallbackInfo ci) {
        this.snowball$die(damageSource);
    }

    @Unique
    protected @Nullable EntityDeathEvent snowball$die(DamageSource damageSource) {
        if (this.snowball$deathTracker == null || !(level() instanceof ServerLevel level)) return null;

        List<io.github.pandier.snowball.entity.ItemEntity> drops = this.snowball$deathTracker.getDrops().stream().map(Conversions.INSTANCE::snowball).collect(Collectors.toList());
        int experience = this.snowball$deathTracker.getExperience();
        Audience audience = this.snowball$deathTracker.getAudience();
        Component message = this.snowball$deathTracker.getMessage() == null ? null : Conversions.Adventure.INSTANCE.adventure(this.snowball$deathTracker.getMessage());

        this.snowball$deathTracker = null;

        EntityDeathEvent event = new EntityDeathEventImpl(
                Conversions.INSTANCE.snowball((LivingEntity) (Object) this),
                Conversions.INSTANCE.snowball(damageSource),
                audience,
                message,
                drops,
                experience
        );

        Snowball.getEventManager().dispatch(event);

        for (io.github.pandier.snowball.entity.ItemEntity entity : event.getDrops()) {
            level.addFreshEntity(((ItemEntityImpl) entity).getAdaptee());
        }

        if (event.getExperience() > 0) {
            ExperienceOrb.award(level, this.position(), event.getExperience());
        }

        if (event.getMessage() != null) {
            event.getAudience().sendMessage(event.getMessage());
        }

        return event;
    }

    @Unique
    protected @Nullable EntityDeathTracker snowball$startDeathTracker() {
        if (this.snowball$deathTracker != null)
            return this.snowball$deathTracker;
        // TODO: Don't track if there are no listeners
        this.snowball$deathTracker = new EntityDeathTracker();
        this.snowball$deathTracker.setMessage(this.getCombatTracker().getDeathMessage());
        return this.snowball$deathTracker;
    }

    @Inject(method = "drop",
            cancellable = true,
            order = 2000,
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    public void inject$drop(ItemStack itemStack, boolean bl, boolean bl2, CallbackInfoReturnable<ItemEntity> cir, @Local ItemEntity itemEntity) {
        if (this.snowball$deathTracker != null) {
            this.snowball$deathTracker.addDrop(itemEntity);
            cir.setReturnValue(itemEntity);
        }
    }

    @Override
    public void inject$spawnAtLocation(ServerLevel level, net.minecraft.world.item.ItemStack stack, Vec3 vec3, CallbackInfoReturnable<ItemEntity> cir, ItemEntity entity) {
        if (this.snowball$deathTracker != null) {
            this.snowball$deathTracker.addDrop(entity);
            cir.setReturnValue(entity);
        }
    }

    @WrapOperation(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"))
    public void inject$dropExperience(ServerLevel level, Vec3 vec3, int i, Operation<Void> original) {
        if (this.snowball$deathTracker != null) {
            this.snowball$deathTracker.addExperience(i);
        } else {
            original.call(level, vec3, i);
        }
    }
}
