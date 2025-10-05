package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.inventory.PlayerInventoryImpl
import io.github.pandier.snowball.inventory.PlayerInventory
import io.github.pandier.snowball.profile.GameProfile
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import net.minecraft.network.message.SentMessage
import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvent

// TODO: Fully implement Audience
open class PlayerImpl(
    adaptee: ServerPlayerEntity
) : LivingEntityImpl(adaptee), Player {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: ServerPlayerEntity = adaptee

    override val gameProfile: GameProfile
        get() = Conversions.snowball(adaptee.gameProfile)

//    override var rotation: Vector2f
//        get() = super.rotation
//        set(value) {
//            adaptee.networkHandler.requestTeleport(PlayerPosition(Vec3d.ZERO, Vec3d.ZERO, value.x, value.y),
//                EnumSet.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z, PositionFlag.DELTA_X,
//                    PositionFlag.DELTA_Y, PositionFlag.DELTA_Z, PositionFlag.ROTATE_DELTA))
//        }

    override val inventory: PlayerInventory = PlayerInventoryImpl(adaptee.inventory)

    override fun sendMessage(message: Component) {
        adaptee.sendMessage(Conversions.Adventure.vanilla(message))
    }

    @Suppress("UnstableApiUsage", "OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        sendMessage(message)
    }

    override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {
        adaptee.sendChatMessage(
            SentMessage.Profileless(Conversions.Adventure.vanilla(message)),
            adaptee.shouldFilterText(),
            Conversions.Adventure.vanilla(boundChatType, adaptee.registryManager)
        )
    }

    override fun sendMessage(signedMessage: SignedMessage, boundChatType: ChatType.Bound) {
        sendMessage(signedMessage.unsignedContent() ?: Component.text(signedMessage.message()), boundChatType)
    }

    override fun sendActionBar(message: Component) {
        adaptee.sendMessage(Conversions.Adventure.vanilla(message), true)
    }

    override fun <T> sendTitlePart(part: TitlePart<T?>, value: T & Any) {
        when (part) {
            TitlePart.TITLE -> adaptee.networkHandler.sendPacket(TitleS2CPacket(Conversions.Adventure.vanilla(value as Component)))
            TitlePart.SUBTITLE -> adaptee.networkHandler.sendPacket(SubtitleS2CPacket(Conversions.Adventure.vanilla(value as Component)))
            TitlePart.TIMES -> {
                val times = value as Title.Times
                adaptee.networkHandler.sendPacket(TitleFadeS2CPacket(
                    Conversions.Adventure.toTicks(times.fadeIn()).toInt(),
                    Conversions.Adventure.toTicks(times.stay()).toInt(),
                    Conversions.Adventure.toTicks(times.fadeOut()).toInt(),
                ))
            }
        }
    }

    override fun clearTitle() {
        adaptee.networkHandler.sendPacket(ClearTitleS2CPacket(false))
    }

    override fun resetTitle() {
        adaptee.networkHandler.sendPacket(ClearTitleS2CPacket(true))
    }

    override fun playSound(sound: Sound) {
        playSound(sound, adaptee.x, adaptee.y, adaptee.z)
    }

    override fun playSound(sound: Sound, x: Double, y: Double, z: Double) {
        val entry = RegistryEntry.of(SoundEvent.of(Conversions.Adventure.vanilla(sound.name())))
        adaptee.networkHandler.sendPacket(PlaySoundS2CPacket(entry, Conversions.Adventure.vanilla(sound.source()),
            adaptee.x, adaptee.y, adaptee.z, sound.volume(), sound.pitch(), sound.seed().orElseGet { adaptee.random.nextLong() }))
    }

    override fun playSound(sound: Sound, emitter: Sound.Emitter) {
        val entry = RegistryEntry.of(SoundEvent.of(Conversions.Adventure.vanilla(sound.name())))
        adaptee.networkHandler.sendPacket(PlaySoundFromEntityS2CPacket(entry, Conversions.Adventure.vanilla(sound.source()),
            Conversions.Adventure.vanilla(emitter, adaptee), sound.volume(), sound.pitch(), sound.seed().orElseGet { adaptee.random.nextLong() }))
    }

    override fun stopSound(stop: SoundStop) {
        adaptee.networkHandler.sendPacket(StopSoundS2CPacket(stop.sound()?.let(Conversions.Adventure::vanilla),
            stop.source()?.let(Conversions.Adventure::vanilla)))
    }
}