package io.github.pandier.snowball.impl.mixin;

import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor("equipment")
    @NotNull EntityEquipment snowball$getEquipment();
}
