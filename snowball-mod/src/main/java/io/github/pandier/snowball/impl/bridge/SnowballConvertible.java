package io.github.pandier.snowball.impl.bridge;

import org.jetbrains.annotations.NotNull;

public interface SnowballConvertible<T> {
    @NotNull T snowball$get();
}
