package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.ResetableComponentAccessBridge;
import net.minecraft.component.ComponentType;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ResetableComponentAccessBridge {
    @Shadow @Final MergedComponentMap components;

    @Override
    public <T> @Nullable T snowball$reset(@NotNull ComponentType<? extends T> type) {
        return ((ResetableComponentAccessBridge) (Object) this.components).snowball$reset(type);
    }
}
