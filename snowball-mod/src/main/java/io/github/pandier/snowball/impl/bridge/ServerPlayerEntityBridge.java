package io.github.pandier.snowball.impl.bridge;

import net.minecraft.text.Text;

public interface ServerPlayerEntityBridge {
    void snowball$setJoinMessage(Text text);
    Text snowball$getJoinMessage();
}
