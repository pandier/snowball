package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.entity.LivingEntityImpl;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Override
    public LivingEntityImpl snowball$createAdapter() {
        return new LivingEntityImpl((LivingEntity) (Object) this);
    }
}
