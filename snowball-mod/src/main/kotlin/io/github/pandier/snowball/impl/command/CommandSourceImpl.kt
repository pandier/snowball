package io.github.pandier.snowball.impl.command

import io.github.pandier.snowball.command.CommandSource
import io.github.pandier.snowball.impl.Conversions
import net.kyori.adventure.text.Component
import net.minecraft.server.command.ServerCommandSource

class CommandSourceImpl(
    val base: ServerCommandSource,
) : CommandSource {
    override fun sendMessage(message: Component) {
        base.sendMessage(Conversions.Adventure.vanilla(message))
    }
}