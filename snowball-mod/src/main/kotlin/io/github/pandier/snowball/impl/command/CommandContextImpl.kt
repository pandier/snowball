package io.github.pandier.snowball.impl.command

import io.github.pandier.snowball.command.CommandContext
import io.github.pandier.snowball.command.CommandSource
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import net.minecraft.commands.CommandSourceStack

class CommandContextImpl(
    override val adaptee: com.mojang.brigadier.context.CommandContext<CommandSourceStack>,
    private val transformedArguments: Map<String, Any?>,
) : SnowballAdapter(), CommandContext{
    override val source: CommandSource = CommandSourceImpl(adaptee.source)

    override fun <T> require(name: String): T {
        return get(name) ?: error("Missing required argument: $name")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(name: String): T? {
        val value = transformedArguments[name] ?: return null
        return value as T
    }
}