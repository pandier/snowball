package io.github.pandier.snowball.impl.command

import io.github.pandier.snowball.command.CommandContext
import io.github.pandier.snowball.command.CommandSource
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import net.minecraft.server.command.ServerCommandSource

class CommandContextImpl(
    override val adaptee: com.mojang.brigadier.context.CommandContext<ServerCommandSource>
) : SnowballAdapter(adaptee), CommandContext<CommandSource> {
    override val source: CommandSource = CommandSourceImpl(adaptee.source)

    override fun <T> require(name: String, clazz: Class<T>): T {
        // TODO: Better error message
        return get(name, clazz)!!
    }

    override fun <T> get(name: String, clazz: Class<T>): T? {
        return adaptee.getArgument(name, clazz)
    }
}