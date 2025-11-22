package io.github.pandier.snowball.impl.bridge;

import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ServerPlayerBridge {
    void snowball$setJoinMessage(Component message);
    Component snowball$getJoinMessage();

    void snowball$setScoreboard(@Nullable ServerScoreboard scoreboard);
    @NotNull ServerScoreboard snowball$getScoreboard();
    void snowball$initializeScoreboard();
}
