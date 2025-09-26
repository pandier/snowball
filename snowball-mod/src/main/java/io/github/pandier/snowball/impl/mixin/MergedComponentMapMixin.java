package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.ResetableComponentAccessBridge;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.MergedComponentMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(MergedComponentMap.class)
public class MergedComponentMapMixin implements ResetableComponentAccessBridge {
    @Shadow @Final private ComponentMap baseComponents;
    @Shadow private Reference2ObjectMap<ComponentType<?>, Optional<?>> changedComponents;

    @SuppressWarnings({"unchecked", "OptionalAssignedToNull"})
    @Override
    public <T> @Nullable T snowball$reset(@NotNull ComponentType<? extends T> type) {
        Optional<? extends T> optional = (Optional<? extends T>) this.changedComponents.remove(type);
        return optional != null ? optional.orElse(null) : this.baseComponents.get(type);
    }
}
