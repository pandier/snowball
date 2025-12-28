package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.entity.projectile.ProjectileImpl;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Projectile.class)
public abstract class ProjectileMixin extends EntityMixin {

    @Override
    public ProjectileImpl snowball$createAdapter() {
        return new ProjectileImpl((Projectile) (Object) this);
    }
}
