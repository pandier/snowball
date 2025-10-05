package io.github.pandier.snowball.impl.server

import com.google.common.collect.Iterables
import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.scheduler.SchedulerImpl
import io.github.pandier.snowball.server.Console
import io.github.pandier.snowball.server.Server
import io.github.pandier.snowball.world.World
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.key.Key
import net.minecraft.core.registries.Registries
import net.minecraft.server.MinecraftServer
import java.util.UUID

class ServerImpl(
    override val adaptee: MinecraftServer,
) : SnowballAdapter(adaptee), Server, ForwardingAudience {
    override val scheduler: SchedulerImpl = SchedulerImpl()

    override val console: Console = ConsoleImpl(this)

    override val overworld: World
        get() = adaptee.overworld().let(Conversions::snowball)

    override val worlds: Iterable<World>
        get() = Iterables.transform(adaptee.allLevels, Conversions::snowball)

    override fun getWorld(key: Key): World? {
        return adaptee.getLevel(Conversions.Adventure.resourceKey(Registries.DIMENSION, key))?.let(Conversions::snowball)
    }

    override val players: Iterable<Player>
        get() = Iterables.transform(adaptee.playerList.players, Conversions::snowball)

    override fun getPlayer(uuid: UUID): Player? {
        return adaptee.playerList.getPlayer(uuid)?.let(Conversions::snowball)
    }

    override fun getPlayer(name: String): Player? {
        return adaptee.playerList.getPlayer(name)?.let(Conversions::snowball)
    }

    override fun audiences(): Iterable<Audience> {
        return Iterables.concat(listOf(console), players)
    }
}
