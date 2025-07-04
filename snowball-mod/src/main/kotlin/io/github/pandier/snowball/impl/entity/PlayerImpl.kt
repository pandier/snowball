package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adventure.AdventureSerializers
import io.github.pandier.snowball.math.Vector2f
import io.github.pandier.snowball.math.Vector3d
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.SoundStop
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import net.minecraft.entity.player.PlayerPosition
import net.minecraft.network.message.FilterMask
import net.minecraft.network.message.MessageBody
import net.minecraft.network.message.MessageLink
import net.minecraft.network.message.SentMessage
import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.text.Text
import java.util.Optional

// TODO: Fully implement Audience
open class PlayerImpl(
    override val adaptee: ServerPlayerEntity
) : Player, LivingEntityImpl(adaptee) {
//    override var rotation: Vector2f
//        get() = super.rotation
//        set(value) {
//            adaptee.networkHandler.requestTeleport(PlayerPosition(Vec3d.ZERO, Vec3d.ZERO, value.x, value.y),
//                EnumSet.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z, PositionFlag.DELTA_X,
//                    PositionFlag.DELTA_Y, PositionFlag.DELTA_Z, PositionFlag.ROTATE_DELTA))
//        }

    override fun sendMessage(message: Component) {
        adaptee.sendMessage(Conversions.Adventure.vanilla(message))
    }

    @Suppress("UnstableApiUsage", "OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        when (type) {
            MessageType.SYSTEM -> adaptee.sendMessage(Conversions.Adventure.vanilla(message))
            MessageType.CHAT -> {
                val content = AdventureSerializers.plain.serialize(message)
                adaptee.sendChatMessage(
                    SentMessage.of(net.minecraft.network.message.SignedMessage(
                        MessageLink.of(source.uuid()),
                        null,
                        MessageBody.ofUnsigned(content),
                        Conversions.Adventure.vanilla(message),
                        FilterMask.PASS_THROUGH
                    )),
                    false,
                    net.minecraft.network.message.MessageType.Parameters(
                        adaptee.registryManager.getEntryOrThrow(net.minecraft.network.message.MessageType.CHAT),
                        Text.literal(source.uuid().toString()),
                        Optional.empty()
                    )
                )
            }
        }
    }

    override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {
        adaptee.sendChatMessage(
            SentMessage.Profileless(Conversions.Adventure.vanilla(message)),
            false,
            Conversions.Adventure.vanilla(boundChatType, adaptee.registryManager)
        )
    }

    override fun sendMessage(signedMessage: SignedMessage, boundChatType: ChatType.Bound) {
        adaptee.sendChatMessage(
            SentMessage.of(Conversions.Adventure.vanilla(signedMessage)),
            false,
            Conversions.Adventure.vanilla(boundChatType, adaptee.registryManager)
        )
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