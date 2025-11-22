package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.inventory.PlayerInventoryImpl;
import io.github.pandier.snowball.entity.player.PlayerInventory;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Inventory.class)
public class InventoryMixin implements SnowballConvertible<PlayerInventory> {
    @Unique private final SnowballAdapterHolder<PlayerInventoryImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new PlayerInventoryImpl((Inventory) (Object) this));

    @Override
    public @NotNull PlayerInventory snowball$get() {
        return impl$adapter.get();
    }
}
