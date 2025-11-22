package io.github.pandier.snowball.impl.bridge;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ServerScoreboardBridge {
    void snowball$addViewer(@NotNull ServerPlayer viewer);
    void snowball$removeViewer(@NotNull ServerPlayer viewer);
    Collection<ServerPlayer> snowball$getViewers();
}
