package io.github.pandier.snowball.impl.command

import io.github.pandier.snowball.command.CommandContext
import io.github.pandier.snowball.command.CommandSource
import net.minecraft.server.command.ServerCommandSource

class CommandContextImpl(
    val base: com.mojang.brigadier.context.CommandContext<ServerCommandSource>
) : CommandContext<CommandSource> {
    override val source: CommandSource = CommandSourceImpl(base.source)

    override fun <T> require(name: String, clazz: Class<T>): T {
        // TODO: Better error message
        return get(name, clazz)!!
    }

    override fun <T> get(name: String, clazz: Class<T>): T? {
        return base.getArgument(name, clazz)
    }
}