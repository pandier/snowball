package io.github.pandier.snowball.impl.bridge;

import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResetableComponentAccessBridge {
    <T> @Nullable T snowball$reset(@NotNull DataComponentType<? extends T> type);
}
