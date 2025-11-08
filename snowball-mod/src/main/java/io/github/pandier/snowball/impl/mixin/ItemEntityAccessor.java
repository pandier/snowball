package io.github.pandier.snowball.impl.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {

    @Accessor("age")
    void snowball$setAge(int age);

    @Accessor("thrower")
    void snowball$setThrower(@Nullable EntityReference<@NotNull Entity> entity);

    @Accessor("pickupDelay")
    int snowball$getPickupDelay();

    @Accessor("target")
    UUID snowball$getTarget();
}
