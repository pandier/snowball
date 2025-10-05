package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.item.ItemTypeImpl;
import io.github.pandier.snowball.item.ItemType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.class)
public class ItemMixin implements SnowballConvertible<ItemType> {
    @Unique private final SnowballAdapterHolder<ItemTypeImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new ItemTypeImpl((Item) (Object) this));

    @Override
    public @NotNull ItemType snowball$get() {
        return impl$adapter.get();
    }
}
