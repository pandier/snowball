package io.github.pandier.snowball.impl.bridge;

import net.minecraft.server.ServerScoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ServerGamePacketListenerImplBridge {
    void snowball$setScoreboard(@Nullable ServerScoreboard scoreboard);
    @NotNull ServerScoreboard snowball$getScoreboard();
    void snowball$initializeScoreboard();
}
