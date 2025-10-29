package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.entity.AttributeTypeImpl;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Attribute.class)
public class AttributeMixin implements SnowballConvertible<AttributeTypeImpl> {
    @Unique private final SnowballAdapterHolder<AttributeTypeImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new AttributeTypeImpl((Attribute) (Object) this));

    @Override
    public @NotNull AttributeTypeImpl snowball$get() {
        return impl$adapter.get();
    }
}
