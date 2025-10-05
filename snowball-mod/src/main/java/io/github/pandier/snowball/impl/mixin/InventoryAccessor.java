package io.github.pandier.snowball.impl.mixin;

import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Inventory.class)
public interface InventoryAccessor {

    @Accessor("equipment")
    EntityEquipment snowball$getEquipment();
}