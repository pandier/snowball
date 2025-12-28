package io.github.pandier.snowball.impl.bridge;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface FireworkRocketEntityBridge {
    void snowball$detonate();
    void snowball$setItem(ItemStack stack);
    @Nullable LivingEntity snowball$getAttachedTo();
    void snowball$setAttachedTo(@Nullable LivingEntity value);
    void snowball$setShotAtAngle(boolean value);
    int snowball$getLife();
    void snowball$setLife(int value);
    int snowball$getLifetime();
    void snowball$setLifetime(int value);
}
