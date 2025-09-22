package io.github.pandier.snowball.impl.server

import com.google.common.collect.Collections2
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
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import java.util.UUID

class ServerImpl(
    override val adaptee: MinecraftServer,
) : SnowballAdapter(adaptee), Server, ForwardingAudience {
    override val scheduler: SchedulerImpl = SchedulerImpl()

    override val console: Console = ConsoleImpl(this)

    override val overworld: World
        get() = adaptee.overworld.let(Conversions::snowball)

    @Suppress("UNCHECKED_CAST")
    override val worlds: Collection<World>
        get() = Collections2.transform(adaptee.worlds as Collection<ServerWorld>, Conversions::snowball)

    override fun getWorld(key: Key): World? {
        return adaptee.getWorld(Conversions.Adventure.registryKey(RegistryKeys.WORLD, key))?.let(Conversions::snowball)
    }

    override val players: Collection<Player>
        get() = Collections2.transform(adaptee.playerManager.playerList, Conversions::snowball)

    override fun getPlayer(uuid: UUID): Player? {
        return adaptee.playerManager.getPlayer(uuid)?.let(Conversions::snowball)
    }

    override fun getPlayer(name: String): Player? {
        return adaptee.playerManager.getPlayer(name)?.let(Conversions::snowball)
    }

    override fun audiences(): Iterable<Audience> {
        return Iterables.concat(listOf(console), players)
    }
}
