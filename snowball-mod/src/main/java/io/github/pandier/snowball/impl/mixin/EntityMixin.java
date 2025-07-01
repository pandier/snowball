package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.entity.EntityImpl;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin implements SnowballConvertible<io.github.pandier.snowball.entity.Entity> {
    @Unique protected @Nullable EntityImpl impl$adapter = null;

    @Unique
    public EntityImpl snowball$createAdapter() {
        return new EntityImpl((Entity) (Object) this);
    }

    @Override
    public @NotNull io.github.pandier.snowball.entity.Entity snowball$get() {
        if (impl$adapter == null)
            impl$adapter = snowball$createAdapter();
        return impl$adapter;
    }
}
