package io.github.pandier.snowball.registry

import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.key.Key

public interface SnowballRegistries {
    public fun blockType(key: Key): RegistryReference<BlockType>
}
