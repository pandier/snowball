package io.github.pandier.snowball.impl.server

import com.google.common.collect.Iterables
import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.SnowballAdapter
import io.github.pandier.snowball.server.Console
import io.github.pandier.snowball.server.Server
import io.github.pandier.snowball.world.World
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.MinecraftServer
import net.minecraft.text.Text
import java.util.Optional
import java.util.UUID

class ServerImpl(
    override val adaptee: MinecraftServer,
) : SnowballAdapter(adaptee), Server, ForwardingAudience {
    override val console: Console = ConsoleImpl()

    override val worlds: Iterable<World>
        get() = Iterables.transform(adaptee.worlds) { Conversions.world(it) }

    override fun getWorld(key: Key): World? {
        return adaptee.getWorld(Conversions.Adventure.registryKey(RegistryKeys.WORLD, key))?.let(Conversions::world)
    }

    override val players: Iterable<Player>
        get() = Iterables.transform(adaptee.playerManager.playerList) { Conversions.player(it) }

    override fun getPlayer(uuid: UUID): Player? {
        return adaptee.playerManager.getPlayer(uuid)?.let(Conversions::player)
    }

    override fun getPlayer(name: String): Player? {
        return adaptee.playerManager.getPlayer(name)?.let(Conversions::player)
    }

    override fun audiences(): Iterable<Audience> {
        return Iterables.concat(listOf(console), players)
    }

    inner class ConsoleImpl : Console {

        override fun sendMessage(message: Component) {
            adaptee.sendMessage(Conversions.Adventure.vanilla(message))
        }

        @Suppress("UnstableApiUsage", "OVERRIDE_DEPRECATION", "DEPRECATION")
        override fun sendMessage(source: Identity, message: Component, type: MessageType) {
            when (type) {
                MessageType.SYSTEM -> sendMessage(message)
                MessageType.CHAT -> adaptee.logChatMessage(
                    Conversions.Adventure.vanilla(message),
                    net.minecraft.network.message.MessageType.Parameters(
                        adaptee.registryManager.getEntryOrThrow(net.minecraft.network.message.MessageType.CHAT),
                        Text.literal(source.uuid().toString()),
                        Optional.empty()
                    ),
                    "Not Secure"
                )
            }
        }

        override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {
            adaptee.logChatMessage(
                Conversions.Adventure.vanilla(message),
                Conversions.Adventure.vanilla(boundChatType, adaptee.registryManager),
                "Not Secure"
            )
        }

        // TODO: Verify instead of assuming that it's Not Secure
        override fun sendMessage(signedMessage: SignedMessage, boundChatType: ChatType.Bound) {
            sendMessage(signedMessage.unsignedContent() ?: Component.text(signedMessage.message()), boundChatType)
        }
    }
}
