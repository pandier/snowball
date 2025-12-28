package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.FireworkRocketEntityBridge;
import io.github.pandier.snowball.impl.entity.projectile.FireworkRocketImpl;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends EntityMixin implements FireworkRocketEntityBridge {
    @Shadow @Final private static EntityDataAccessor<@NotNull Boolean> DATA_SHOT_AT_ANGLE;
    @Shadow @Final private static EntityDataAccessor<@NotNull ItemStack> DATA_ID_FIREWORKS_ITEM;
    @Shadow protected abstract void explode(ServerLevel serverLevel);
    @Shadow private @Nullable LivingEntity attachedToEntity;
    @Shadow private int life;
    @Shadow private int lifetime;

    @Override
    public FireworkRocketImpl snowball$createAdapter() {
        return new FireworkRocketImpl((FireworkRocketEntity) (Object) this);
    }

    @Override
    public void snowball$detonate() {
        if (this.level() instanceof ServerLevel level) {
            this.explode(level);
        }
    }

    @Override
    public void snowball$setItem(ItemStack stack) {
        this.entityData.set(DATA_ID_FIREWORKS_ITEM, stack);
    }

    @Override
    public void snowball$setAttachedTo(LivingEntity value) {
        this.attachedToEntity = value;
    }

    @Override
    public LivingEntity snowball$getAttachedTo() {
        return this.attachedToEntity;
    }

    @Override
    public void snowball$setShotAtAngle(boolean value) {
        this.entityData.set(DATA_SHOT_AT_ANGLE, value);
    }

    @Override
    public int snowball$getLife() {
        return this.life;
    }

    @Override
    public void snowball$setLife(int value) {
        this.life = value;
    }

    @Override
    public int snowball$getLifetime() {
        return this.lifetime;
    }

    @Override
    public void snowball$setLifetime(int value) {
        this.lifetime = value;
    }
}
