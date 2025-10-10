package io.github.pandier.snowball.impl.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSource.class)
public interface DamageSourceAccessor {

    @Invoker("<init>")
    static DamageSource snowball$init(Holder<DamageType> holder, @Nullable Entity entity, @Nullable Entity entity2, @Nullable Vec3 vec3) {
        throw new AssertionError();
    }
}
