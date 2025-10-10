package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.entity.damage.DamageTypeImpl;
import net.minecraft.world.damagesource.DamageType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageType.class)
public class DamageTypeMixin implements SnowballConvertible<io.github.pandier.snowball.entity.damage.DamageType> {
    @Unique private final SnowballAdapterHolder<DamageTypeImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new DamageTypeImpl((DamageType) (Object) this));

    @Override
    public io.github.pandier.snowball.entity.damage.@NotNull DamageType snowball$get() {
        return impl$adapter.get();
    }
}
