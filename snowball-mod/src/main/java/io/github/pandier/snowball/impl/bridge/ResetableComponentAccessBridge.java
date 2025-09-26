package io.github.pandier.snowball.impl.bridge;

import net.minecraft.component.ComponentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResetableComponentAccessBridge {
    <T> @Nullable T snowball$reset(@NotNull ComponentType<? extends T> type);
}
