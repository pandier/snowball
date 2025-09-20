package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.inventory.InventoryImpl;
import net.minecraft.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Inventory.class)
public interface InventoryMixin extends SnowballConvertible<InventoryImpl> {

    @Override
    default @NotNull InventoryImpl snowball$get() {
        return new InventoryImpl((Inventory) this);
    }
}
