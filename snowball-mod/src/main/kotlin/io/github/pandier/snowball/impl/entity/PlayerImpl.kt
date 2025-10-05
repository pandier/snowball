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
import net.minecraft.core.Holder
import net.minecraft.network.chat.OutgoingChatMessage
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent

// TODO: Fully implement Audience
open class PlayerImpl(
    adaptee: ServerPlayer
) : LivingEntityImpl(adaptee), Player {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: ServerPlayer = adaptee

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
            TitlePart.SUBTITLE -> adaptee.connection.send(ClientboundSetSubtitleTextPacket(Conversions.Adventure.vanilla(value as Component)))
            TitlePart.TIMES -> {
                val times = value as Title.Times
                adaptee.connection.send(ClientboundSetTitlesAnimationPacket(
                    Conversions.Adventure.toTicks(times.fadeIn()).toInt(),
                    Conversions.Adventure.toTicks(times.stay()).toInt(),
                    Conversions.Adventure.toTicks(times.fadeOut()).toInt(),
                ))
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
        adaptee.connection.send(ClientboundSoundPacket(entry, Conversions.Adventure.vanilla(sound.source()),
            adaptee.x, adaptee.y, adaptee.z, sound.volume(), sound.pitch(), sound.seed().orElseGet { adaptee.random.nextLong() }))
    }

    override fun playSound(sound: Sound, emitter: Sound.Emitter) {
        val entry = Holder.direct(SoundEvent.createVariableRangeEvent(Conversions.Adventure.vanilla(sound.name())))
        adaptee.connection.send(ClientboundSoundEntityPacket(entry, Conversions.Adventure.vanilla(sound.source()),
            Conversions.Adventure.vanilla(emitter, adaptee), sound.volume(), sound.pitch(), sound.seed().orElseGet { adaptee.random.nextLong() }))
    }

    override fun stopSound(stop: SoundStop) {
        adaptee.connection.send(ClientboundStopSoundPacket(stop.sound()?.let(Conversions.Adventure::vanilla),
            stop.source()?.let(Conversions.Adventure::vanilla)))
    }
}