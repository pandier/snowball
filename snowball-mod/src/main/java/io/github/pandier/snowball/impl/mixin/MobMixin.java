package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.entity.MobImpl;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntityMixin {

    @Override
    public MobImpl snowball$createAdapter() {
        return new MobImpl((Mob) (Object) this);
    }
}
