package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.entity.EntityImpl;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin implements SnowballConvertible<io.github.pandier.snowball.entity.Entity> {
    @Shadow public int invulnerableTime;

    @Unique protected SnowballAdapterHolder<EntityImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(this::snowball$createAdapter);

    @Unique
    public EntityImpl snowball$createAdapter() {
        return new EntityImpl((Entity) (Object) this);
    }

    @Override
    public @NotNull io.github.pandier.snowball.entity.Entity snowball$get() {
        return impl$adapter.get();
    }
}
