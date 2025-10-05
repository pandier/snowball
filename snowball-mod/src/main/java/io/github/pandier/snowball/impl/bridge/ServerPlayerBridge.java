package io.github.pandier.snowball.impl.bridge;

import net.minecraft.network.chat.Component;

public interface ServerPlayerBridge {
    void snowball$setJoinMessage(Component message);
    Component snowball$getJoinMessage();
}
