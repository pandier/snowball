package io.github.pandier.snowball.impl.entity.player

import io.github.pandier.snowball.entity.player.GameMode
import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adventure.SnowballBossBarImplementation
import io.github.pandier.snowball.impl.entity.LivingEntityImpl
import io.github.pandier.snowball.impl.inventory.PlayerInventoryImpl
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.inventory.PlayerInventory
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.profile.GameProfile
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBarImplementation
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import net.minecraft.core.Holder
import net.minecraft.network.chat.OutgoingChatMessage
import net.minecraft.network.protocol.game.*
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.PositionMoveRotation
import net.minecraft.world.entity.Relative
import net.minecraft.world.phys.Vec3
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.UnmodifiableView
import java.util.Collections

// TODO: Fully implement Audience
open class PlayerImpl(
    adaptee: ServerPlayer
) : LivingEntityImpl(adaptee), Player {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: ServerPlayer = adaptee

    private var bossBars: MutableList<BossBar>? = null

    override val gameProfile: GameProfile
        get() = Conversions.snowball(adaptee.gameProfile)

    override var gameMode: GameMode
        get() = adaptee.gameMode().let(Conversions::snowball)
        set(value) {
            adaptee.setGameMode(value.let(Conversions::vanilla))
        }

//    override var rotation: Vector2f
//        get() = super.rotation
//        set(value) {
//            adaptee.networkHandler.requestTeleport(PlayerPosition(Vec3d.ZERO, Vec3d.ZERO, value.x, value.y),
//                EnumSet.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z, PositionFlag.DELTA_X,
//                    PositionFlag.DELTA_Y, PositionFlag.DELTA_Z, PositionFlag.ROTATE_DELTA))
//        }

    override var velocity: Vector3d
        get() = super.velocity
        set(value) {
            adaptee.connection.teleport(
                PositionMoveRotation(Vec3.ZERO, Vec3(value.x, value.y, value.z), 0f, 0f),
                setOf(Relative.X, Relative.Y, Relative.Z, Relative.X_ROT, Relative.Y_ROT, Relative.ROTATE_DELTA)
            )
        }

    override val inventory: PlayerInventory = PlayerInventoryImpl(adaptee.inventory)

    override fun give(stack: ItemStack, silent: Boolean): Int {
        val amount = inventory.insert(stack)
        if (amount > 0 && !silent) {
            adaptee.level().playSound(null, adaptee.x, adaptee.y, adaptee.z,
                    SoundEvents.ITEM_PICKUP,  SoundSource.PLAYERS, 0.2f,
                    ((adaptee.random.nextFloat() - adaptee.random.nextFloat()) * 0.7f + 1.0f) * 2.0f)
        }
        return amount
    }

    override fun giveOrDrop(stack: ItemStackView, silent: Boolean) {
        val stack = stack.copy()
        give(stack, silent)

        if (stack.isEmpty()) return

        val itemEntity = adaptee.drop((stack as ItemStackImpl).adaptee, false)
        if (itemEntity != null) {
            itemEntity.setNoPickUpDelay()
            itemEntity.setTarget(adaptee.uuid)
        }
    }

    override fun sendMessage(message: Component) {
        adaptee.sendSystemMessage(Conversions.Adventure.vanilla(message))
    }

    @Suppress("UnstableApiUsage", "OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        sendMessage(message)
    }

    override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {
        adaptee.sendChatMessage(
            OutgoingChatMessage.Disguised(Conversions.Adventure.vanilla(message)),
            adaptee.isTextFilteringEnabled,
            Conversions.Adventure.vanilla(boundChatType, adaptee.registryAccess())
        )
    }

    override fun sendMessage(signedMessage: SignedMessage, boundChatType: ChatType.Bound) {
        sendMessage(signedMessage.unsignedContent() ?: Component.text(signedMessage.message()), boundChatType)
    }

    override fun sendActionBar(message: Component) {
        adaptee.sendSystemMessage(Conversions.Adventure.vanilla(message), true)
    }

    override fun <T> sendTitlePart(part: TitlePart<T?>, value: T & Any) {
        when (part) {
            TitlePart.TITLE -> adaptee.connection.send(ClientboundSetTitleTextPacket(Conversions.Adventure.vanilla(value as Component)))
            TitlePart.SUBTITLE -> adaptee.connection.send(
                ClientboundSetSubtitleTextPacket(
                    Conversions.Adventure.vanilla(
                        value as Component
                    )
                )
            )
            TitlePart.TIMES -> {
                val times = value as Title.Times
                adaptee.connection.send(
                    ClientboundSetTitlesAnimationPacket(
                        Conversions.Adventure.toTicks(times.fadeIn()).toInt(),
                        Conversions.Adventure.toTicks(times.stay()).toInt(),
                        Conversions.Adventure.toTicks(times.fadeOut()).toInt(),
                    )
                )
            }
        }
    }

    override fun clearTitle() {
        adaptee.connection.send(ClientboundClearTitlesPacket(false))
    }

    override fun resetTitle() {
        adaptee.connection.send(ClientboundClearTitlesPacket(true))
    }

    override fun playSound(sound: Sound) {
        playSound(sound, adaptee.x, adaptee.y, adaptee.z)
    }

    override fun playSound(sound: Sound, x: Double, y: Double, z: Double) {
        val entry = Holder.direct(SoundEvent.createVariableRangeEvent(Conversions.Adventure.vanilla(sound.name())))
        adaptee.connection.send(
            ClientboundSoundPacket(
                entry,
                Conversions.Adventure.vanilla(sound.source()),
                adaptee.x,
                adaptee.y,
                adaptee.z,
                sound.volume(),
                sound.pitch(),
                sound.seed().orElseGet { adaptee.random.nextLong() })
        )
    }

    override fun playSound(sound: Sound, emitter: Sound.Emitter) {
        val entry = Holder.direct(SoundEvent.createVariableRangeEvent(Conversions.Adventure.vanilla(sound.name())))
        adaptee.connection.send(
            ClientboundSoundEntityPacket(
                entry,
                Conversions.Adventure.vanilla(sound.source()),
                Conversions.Adventure.vanilla(emitter, adaptee),
                sound.volume(),
                sound.pitch(),
                sound.seed().orElseGet { adaptee.random.nextLong() })
        )
    }

    override fun stopSound(stop: SoundStop) {
        adaptee.connection.send(
            ClientboundStopSoundPacket(
                stop.sound()?.let(Conversions.Adventure::vanilla),
                stop.source()?.let(Conversions.Adventure::vanilla)
            )
        )
    }

    @Suppress("UnstableApiUsage")
    override fun showBossBar(bar: BossBar) {
        BossBarImplementation.get(bar, SnowballBossBarImplementation::class.java)
            .addPlayer(adaptee)

        val bossBars = this.bossBars ?: mutableListOf<BossBar>().also { this.bossBars = it }
        bossBars.add(bar)
    }

    @Suppress("UnstableApiUsage")
    override fun hideBossBar(bar: BossBar) {
        BossBarImplementation.get(bar, SnowballBossBarImplementation::class.java)
            .removePlayer(adaptee)

        val bossBars = this.bossBars
        if (bossBars != null) {
            bossBars.remove(bar)
            if (bossBars.isEmpty()) {
                this.bossBars = null
            }
        }
    }

    override fun activeBossBars(): @UnmodifiableView Iterable<BossBar> {
        return this.bossBars?.let(Collections::unmodifiableList) ?: Collections.emptyList()
    }

    /**
     * Called when the player disconnects.
     */
    @ApiStatus.Internal
    fun disconnect() {
        for (bar in activeBossBars().toList()) {
            hideBossBar(bar)
        }
    }

    override fun remove() {
        error("A player cannot be removed")
    }
}