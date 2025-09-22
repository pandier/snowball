package io.github.pandier.snowball.server

import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.scheduler.Scheduler
import io.github.pandier.snowball.world.World
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key
import java.util.UUID

public interface Server : Audience {
    public val scheduler: Scheduler

    public val console: Console

    public val overworld: World

    public val worlds: Collection<World>

    public fun getWorld(key: Key): World?

    public val players: Collection<Player>

    public fun getPlayer(uuid: UUID): Player?
    public fun getPlayer(name: String): Player?
}
