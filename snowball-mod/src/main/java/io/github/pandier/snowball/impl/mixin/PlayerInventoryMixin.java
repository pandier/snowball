package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.inventory.PlayerInventoryImpl;
import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements SnowballConvertible<io.github.pandier.snowball.inventory.PlayerInventory> {

    @Override
    public io.github.pandier.snowball.inventory.@NotNull PlayerInventory snowball$get() {
        return new PlayerInventoryImpl((PlayerInventory) (Object) this);
    }
}
