package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.entity.EntityTypeImpl;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public class EntityTypeMixin implements SnowballConvertible<io.github.pandier.snowball.entity.EntityType<?>> {
    @Unique private final SnowballAdapterHolder<EntityTypeImpl<?, ?>> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new EntityTypeImpl<>((EntityType<?>) (Object) this));

    @Override
    public io.github.pandier.snowball.entity.@NotNull EntityType<?> snowball$get() {
        return impl$adapter.get();
    }
}
