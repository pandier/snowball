package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.entity.EntityImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class EntityMixin implements SnowballConvertible<io.github.pandier.snowball.entity.Entity> {
    @Shadow public int invulnerableTime;
    @Shadow public abstract Level level();
    @Shadow public abstract Vec3 position();
    @Shadow public abstract @Nullable PlayerTeam getTeam();
    @Shadow public abstract int getId();

    @Unique protected @Nullable EntityImpl impl$adapter = null;

    @Unique
    public EntityImpl snowball$createAdapter() {
        return new EntityImpl((Entity) (Object) this);
    }

    @Override
    public @NotNull io.github.pandier.snowball.entity.Entity snowball$get() {
        if (this.impl$adapter == null) {
            this.impl$adapter = this.snowball$createAdapter();
        }
        return this.impl$adapter;
    }

    @SuppressWarnings("CancellableInjectionUsage")
    @Inject(method = "spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/entity/item/ItemEntity;",
            order = 2000,
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD, // Sadly @Local makes CallbackInfoReturnable null for some reason
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    public void inject$spawnAtLocation(ServerLevel serverLevel, ItemStack itemStack, Vec3 vec3, CallbackInfoReturnable<ItemEntity> cir, ItemEntity entity) {
        // Used in LivingEntity to capture death drops
    }

    @Inject(method = "restoreFrom", at = @At("HEAD"))
    public void inject$restoreFrom(Entity oldEntity, CallbackInfo ci) {
        EntityImpl adapter = ((EntityMixin) (Object) oldEntity).impl$adapter;
        if (adapter != null) {
            adapter.updateAdapatee((Entity) (Object) this);
            this.impl$adapter = adapter;
            ((EntityMixin) (Object) oldEntity).impl$adapter = null;
        }
    }
}
