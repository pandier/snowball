package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.inventory.InventoryImpl;
import net.minecraft.world.Container;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Container.class)
public interface ContainerMixin extends SnowballConvertible<InventoryImpl> {

    @Override
    default @NotNull InventoryImpl snowball$get() {
        return new InventoryImpl((Container) this);
    }
}
