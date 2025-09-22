package io.github.pandier.snowball.impl.server

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.server.Console
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.minecraft.text.Text
import java.util.Optional

class ConsoleImpl(
    private val server: ServerImpl,
) : Console {
    override fun sendMessage(message: Component) {
        server.adaptee.sendMessage(Conversions.Adventure.vanilla(message))
    }

    @Suppress("UnstableApiUsage", "OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        when (type) {
            MessageType.SYSTEM -> sendMessage(message)
            MessageType.CHAT -> server.adaptee.logChatMessage(
                Conversions.Adventure.vanilla(message),
                net.minecraft.network.message.MessageType.Parameters(
                    server.adaptee.registryManager.getEntryOrThrow(net.minecraft.network.message.MessageType.CHAT),
                    Text.literal(source.uuid().toString()),
                    Optional.empty()
                ),
                "Not Secure"
            )
        }
    }

    override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {
        server.adaptee.logChatMessage(
            Conversions.Adventure.vanilla(message),
            Conversions.Adventure.vanilla(boundChatType, server.adaptee.registryManager),
            "Not Secure"
        )
    }

    // TODO: Verify instead of assuming that it's Not Secure
    override fun sendMessage(signedMessage: SignedMessage, boundChatType: ChatType.Bound) {
        sendMessage(signedMessage.unsignedContent() ?: Component.text(signedMessage.message()), boundChatType)
    }
}