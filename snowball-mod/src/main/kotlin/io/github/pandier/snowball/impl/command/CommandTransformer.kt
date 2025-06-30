package io.github.pandier.snowball.impl.command

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.pandier.snowball.command.Command
import io.github.pandier.snowball.command.CommandBranch
import io.github.pandier.snowball.command.type.ArgumentType
import io.github.pandier.snowball.command.type.BooleanArgumentType
import io.github.pandier.snowball.command.type.DoubleArgumentType
import io.github.pandier.snowball.command.type.FloatArgumentType
import io.github.pandier.snowball.command.type.GreedyStringArgumentType
import io.github.pandier.snowball.command.type.IntArgumentType
import io.github.pandier.snowball.command.type.LongArgumentType
import io.github.pandier.snowball.command.type.StringArgumentType
import io.github.pandier.snowball.command.type.WordArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

internal object CommandTransformer {

    fun command(command: Command): LiteralArgumentBuilder<ServerCommandSource> {
        return CommandManager.literal(command.name).also {
            branch(command, it)
        }
    }

    private fun branch(branch: CommandBranch, builder: ArgumentBuilder<ServerCommandSource, *>) {
        var lastBuilder = branch.arguments.lastOrNull()
            ?.let { CommandManager.argument(it.name, argument(it.type)) }
            ?: builder

        val executor = branch.executor
        if (executor != null) {
            lastBuilder.executes {
                executor.apply { CommandContextImpl(it).execute() }
                return@executes 0
            }
        }

        for (child in branch.children) {
            val name = child.name
            if (name == null) {
                branch(child, lastBuilder)
            } else {
                lastBuilder.then(CommandManager.literal(name).also {
                    branch(child, it)
                })
            }
        }

        if (branch.arguments.isNotEmpty()) {
            for (i in branch.arguments.size - 2 downTo 0) {
                val argument = branch.arguments[i]
                lastBuilder = CommandManager.argument(argument.name, argument(argument.type)).also {
                    it.then(lastBuilder)
                }
            }
            builder.then(lastBuilder)
        }
    }

    private fun argument(type: ArgumentType<*>): com.mojang.brigadier.arguments.ArgumentType<*> {
        return when (type) {
            is BooleanArgumentType -> BoolArgumentType.bool()
            is IntArgumentType -> IntegerArgumentType.integer(type.min, type.max)
            is LongArgumentType -> com.mojang.brigadier.arguments.LongArgumentType.longArg(type.min, type.max)
            is FloatArgumentType -> com.mojang.brigadier.arguments.FloatArgumentType.floatArg(type.min)
            is DoubleArgumentType -> com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg()
            is StringArgumentType -> com.mojang.brigadier.arguments.StringArgumentType.string()
            is WordArgumentType -> com.mojang.brigadier.arguments.StringArgumentType.word()
            is GreedyStringArgumentType -> com.mojang.brigadier.arguments.StringArgumentType.greedyString()
            else -> error("Unrecognizable argument type: $type")
        }
    }
}