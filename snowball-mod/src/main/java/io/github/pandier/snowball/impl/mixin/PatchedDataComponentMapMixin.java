package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.ResetableComponentAccessBridge;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(PatchedDataComponentMap.class)
public class PatchedDataComponentMapMixin implements ResetableComponentAccessBridge {
    @Shadow @Final private DataComponentMap prototype;
    @Shadow private Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch;

    @SuppressWarnings({"unchecked", "OptionalAssignedToNull"})
    @Override
    public <T> @Nullable T snowball$reset(@NotNull DataComponentType<? extends T> type) {
        Optional<? extends T> optional = (Optional<? extends T>) this.patch.remove(type);
        return optional != null ? optional.orElse(null) : this.prototype.get(type);
    }
}
