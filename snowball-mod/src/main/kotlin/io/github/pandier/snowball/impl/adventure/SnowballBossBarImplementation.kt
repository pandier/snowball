package io.github.pandier.snowball.impl.adventure

import com.google.common.collect.Iterables
import io.github.pandier.snowball.impl.Conversions
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBarImplementation
import net.kyori.adventure.bossbar.BossBarViewer
import net.kyori.adventure.text.Component
import net.minecraft.server.level.ServerBossEvent
import net.minecraft.server.level.ServerPlayer

@Suppress("UnstableApiUsage")
class SnowballBossBarImplementation(private val bossBar: BossBar) : BossBarImplementation, BossBar.Listener {
    private var vanilla: ServerBossEvent? = null

    fun addPlayer(player: ServerPlayer) {
        val vanilla = this.vanilla ?: run {
            ServerBossEvent(
                bossBar.name().let(Conversions.Adventure::vanilla),
                bossBar.color().let(Conversions.Adventure::vanilla),
                bossBar.overlay().let(Conversions.Adventure::vanilla),
            ).also {
                this.vanilla = it

                for (flag in bossBar.flags()) {
                    setFlag(flag, true)
                }

                bossBar.addListener(this)
            }
        }

        vanilla.addPlayer(player)
    }

    fun removePlayer(player: ServerPlayer) {
        val vanilla = this.vanilla ?: return

        vanilla.removePlayer(player)

        if (vanilla.players.isEmpty()) {
            this.vanilla = null

            bossBar.removeListener(this)
        }
    }

    private fun setFlag(flag: BossBar.Flag, value: Boolean) {
        when (flag) {
            BossBar.Flag.PLAY_BOSS_MUSIC -> vanilla?.setPlayBossMusic(value)
            BossBar.Flag.DARKEN_SCREEN -> vanilla?.setDarkenScreen(value)
            BossBar.Flag.CREATE_WORLD_FOG -> vanilla?.setCreateWorldFog(value)
        }
    }

    override fun viewers(): Iterable<BossBarViewer> {
        val players = vanilla?.players ?: return emptyList()
        return Iterables.transform(players, Conversions::snowball)
    }

    override fun bossBarNameChanged(bar: BossBar, oldName: Component, newName: Component) {
        vanilla?.setName(newName.let(Conversions.Adventure::vanilla))
    }

    override fun bossBarProgressChanged(bar: BossBar, oldProgress: Float, newProgress: Float) {
        vanilla?.setProgress(newProgress)
    }

    override fun bossBarColorChanged(bar: BossBar, oldColor: BossBar.Color, newColor: BossBar.Color) {
        vanilla?.setColor(newColor.let(Conversions.Adventure::vanilla))
    }

    override fun bossBarOverlayChanged(bar: BossBar, oldOverlay: BossBar.Overlay, newOverlay: BossBar.Overlay) {
        vanilla?.setOverlay(newOverlay.let(Conversions.Adventure::vanilla))
    }

    override fun bossBarFlagsChanged(bar: BossBar, flagsAdded: Set<BossBar.Flag>, flagsRemoved: Set<BossBar.Flag>) {
        for (flag in flagsRemoved) {
            setFlag(flag, false)
        }

        for (flag in flagsAdded) {
            setFlag(flag, true)
        }
    }
}