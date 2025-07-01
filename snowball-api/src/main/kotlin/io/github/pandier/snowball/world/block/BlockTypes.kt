package io.github.pandier.snowball.world.block

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.registry.RegistryReference
import net.kyori.adventure.key.Key

public object BlockTypes {
    public val AIR: RegistryReference<BlockType> = ref("air")
    public val STONE: RegistryReference<BlockType> = ref("stone")
    public val DIRT: RegistryReference<BlockType> = ref("dirt")
    public val GLASS: RegistryReference<BlockType> = ref("glass")

    private fun ref(id: String): RegistryReference<BlockType> {
        return Snowball.get().registries.blockType(Key.key(Key.MINECRAFT_NAMESPACE, id))
    }
}