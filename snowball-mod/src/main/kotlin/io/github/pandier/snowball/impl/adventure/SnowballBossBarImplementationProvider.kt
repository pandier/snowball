package io.github.pandier.snowball.impl.adventure

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBarImplementation

@Suppress("UnstableApiUsage")
class SnowballBossBarImplementationProvider : BossBarImplementation.Provider {

    override fun create(bar: BossBar): BossBarImplementation {
        return SnowballBossBarImplementation(bar)
    }
}
