package io.github.pandier.snowball.impl

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.adventure.AdventureText
import io.github.pandier.snowball.impl.adventure.VanillaComponentSerializer
import io.github.pandier.snowball.impl.bridge.SnowballConvertible
import io.github.pandier.snowball.impl.entity.EntityImpl
import io.github.pandier.snowball.profile.GameProfile
import io.github.pandier.snowball.profile.GameProfileProperty
import io.github.pandier.snowball.server.Server
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockState
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Ticks
import net.minecraft.block.Block
import net.minecraft.network.message.FilterMask
import net.minecraft.network.message.LastSeenMessageList
import net.minecraft.network.message.MessageBody
import net.minecraft.network.message.MessageLink
import net.minecraft.network.message.MessageSignatureData
import net.minecraft.network.message.MessageType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import java.time.Duration
import java.util.Optional

object Conversions {
    fun snowball(gameProfile: com.mojang.authlib.GameProfile): GameProfile =
        GameProfile(gameProfile.id, gameProfile.name, gameProfile.properties.values().map(::snowball))

    fun vanilla(gameProfile: GameProfile): com.mojang.authlib.GameProfile {
        return com.mojang.authlib.GameProfile(gameProfile.uuid, gameProfile.name).apply {
            for (property in gameProfile.properties) {
                properties.put(property.name, vanilla(property))
            }
        }
    }

    fun snowball(property: com.mojang.authlib.properties.Property): GameProfileProperty =
        GameProfileProperty(property.name, property.value, property.signature)

    fun vanilla(property: GameProfileProperty): com.mojang.authlib.properties.Property =
        com.mojang.authlib.properties.Property(property.name, property.value, property.signature)

    fun snowball(entity: net.minecraft.entity.Entity): Entity = convertible(entity)
    fun snowball(player: ServerPlayerEntity): Player = convertible(player)
    fun snowball(world: ServerWorld): World = convertible(world)
    fun snowball(block: Block): BlockType = convertible(block)
    fun snowball(state: net.minecraft.block.BlockState): BlockState = convertible(state)
    fun snowball(obj: MinecraftServer): Server = convertible(obj)

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> convertible(any: Any): T = (any as SnowballConvertible<T>).`snowball$get`()

    object Adventure {
        fun vanilla(key: Key): Identifier {
            return Identifier.of(key.namespace(), key.value())
        }

        fun adventure(identifier: Identifier): Key {
            return Key.key(identifier.namespace, identifier.path)
        }

        inline fun <reified T> registryKey(registry: RegistryKey<out Registry<T>>, key: Key): RegistryKey<T> {
            return RegistryKey.of(registry, vanilla(key))
        }

        fun toTicks(duration: Duration): Long {
            return duration.toMillis() / Ticks.SINGLE_TICK_DURATION_MS
        }

        fun vanilla(component: Component): Text {
            return AdventureText(component)
        }

        fun adventure(text: Text): Component {
            return VanillaComponentSerializer.deserialize(text)
        }

        fun vanilla(bound: ChatType.Bound, registryManager: DynamicRegistryManager): MessageType.Parameters {
            return MessageType.Parameters(
                registryManager.getEntryOrThrow(registryKey(RegistryKeys.MESSAGE_TYPE, bound.type().key())),
                vanilla(bound.name()),
                Optional.ofNullable(bound.target()?.let(::vanilla)),
            )
        }

        fun vanilla(source: Sound.Source): SoundCategory {
            return when (source) {
                Sound.Source.MASTER -> SoundCategory.MASTER
                Sound.Source.MUSIC -> SoundCategory.MUSIC
                Sound.Source.RECORD -> SoundCategory.RECORDS
                Sound.Source.WEATHER -> SoundCategory.WEATHER
                Sound.Source.BLOCK -> SoundCategory.BLOCKS
                Sound.Source.HOSTILE -> SoundCategory.HOSTILE
                Sound.Source.NEUTRAL -> SoundCategory.NEUTRAL
                Sound.Source.PLAYER -> SoundCategory.PLAYERS
                Sound.Source.AMBIENT -> SoundCategory.AMBIENT
                Sound.Source.VOICE -> SoundCategory.VOICE
                Sound.Source.UI -> SoundCategory.UI
            }
        }

        fun vanilla(emitter: Sound.Emitter, self: net.minecraft.entity.Entity): net.minecraft.entity.Entity {
            if (emitter == Sound.Emitter.self()) return self
            if (emitter is EntityImpl) return emitter.adaptee
            error("Unrecognizable emitter: ${emitter.javaClass}")
        }
    }
}