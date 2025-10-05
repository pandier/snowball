package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.ResetableComponentAccessBridge;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ResetableComponentAccessBridge {
    @Shadow @Final PatchedDataComponentMap components;

    @Override
    public <T> @Nullable T snowball$reset(@NotNull DataComponentType<? extends T> type) {
        return ((ResetableComponentAccessBridge) (Object) this.components).snowball$reset(type);
    }
}
