package io.github.pandier.snowball.impl.world

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.SnowballAdapter
import io.github.pandier.snowball.world.World
import net.kyori.adventure.key.Key
import net.minecraft.server.world.ServerWorld

class WorldImpl(
    override val adaptee: ServerWorld
) : SnowballAdapter(adaptee), World {
    override fun key(): Key {
        return Conversions.Adventure.adventure(adaptee.registryKey.value)
    }
}