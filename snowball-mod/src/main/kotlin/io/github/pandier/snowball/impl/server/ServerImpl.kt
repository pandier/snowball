package io.github.pandier.snowball.impl.server

import com.google.common.collect.Collections2
import com.google.common.collect.Iterables
import com.google.common.collect.Sets
import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
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
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import java.util.Optional
import java.util.UUID

class ServerImpl(
    override val adaptee: MinecraftServer,
) : SnowballAdapter(adaptee), Server, ForwardingAudience {
    override val console: Console = ConsoleImpl()

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
