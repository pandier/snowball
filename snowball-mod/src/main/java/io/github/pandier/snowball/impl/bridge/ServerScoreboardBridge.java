package io.github.pandier.snowball.impl.bridge;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ServerScoreboardBridge {
    void snowball$addViewer(@NotNull ServerGamePacketListenerImpl viewer);
    void snowball$removeViewer(@NotNull ServerGamePacketListenerImpl viewer);
    Collection<ServerGamePacketListenerImpl> snowball$getViewers();
}
