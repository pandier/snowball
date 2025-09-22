package io.github.pandier.snowball.impl.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.github.pandier.snowball.command.Argument
import io.github.pandier.snowball.command.Command
import io.github.pandier.snowball.command.CommandBranch
import io.github.pandier.snowball.command.CommandContext
import io.github.pandier.snowball.command.CommandExecutor
import io.github.pandier.snowball.command.type.ArgumentType
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class CommandTransformer(
    private val registryAccess: CommandRegistryAccess
) {
    fun transform(command: Command): LiteralArgumentBuilder<ServerCommandSource> {
        return CommandManager.literal(command.name).also {
            transformBranch(command, it, listOf())
        }
    }

    private fun transformBranch(branch: CommandBranch, builder: ArgumentBuilder<ServerCommandSource, *>, previousArguments: List<Argument<*>>) {
        val totalArguments = previousArguments + branch.arguments

        var lastBuilder = branch.arguments.lastOrNull()
            ?.let { CommandManager.argument(it.name, getBrigadierType(it.type)) }
            ?: builder

        val executor = branch.executor

        if (executor != null) {
            lastBuilder.executes(transformExecutor(executor, totalArguments))
        }

        for (child in branch.children) {
            val name = child.name
            if (name == null) {
                transformBranch(child, lastBuilder, totalArguments)
            } else {
                val literal = CommandManager.literal(name)
                transformBranch(child, literal, totalArguments)
                lastBuilder.then(literal)
            }
        }

        if (branch.arguments.isNotEmpty()) {
            var parentArgument = branch.arguments.last()

            for (i in branch.arguments.size - 2 downTo 0) {
                val argument = branch.arguments[i]

                lastBuilder = CommandManager.argument(argument.name, getBrigadierType(argument.type)).also {
                    it.then(lastBuilder)
                }

                // Add an executor to the current argument node if the parent argument is optional
                if (parentArgument.optional && executor != null) {
                    lastBuilder.executes(transformExecutor(executor, previousArguments + branch.arguments.subList(0, i + 1)))
                }

                parentArgument = argument
            }

            builder.then(lastBuilder)

            // Add an executor to the root node if the parent argument is optional and no executor was provided
            if (parentArgument.optional && executor != null && builder.command == null) {
                builder.executes(transformExecutor(executor, previousArguments))
            }
        }
    }

    private fun transformExecutor(executor: CommandExecutor, arguments: List<Argument<*>>): com.mojang.brigadier.Command<ServerCommandSource> {
        val transformers = arguments.associate { it.name to ArgumentTransformers.get(it.type) }
        return com.mojang.brigadier.Command { context ->
            executor.run { transformContext(context, transformers).execute().value }
        }
    }

    private fun transformContext(context: com.mojang.brigadier.context.CommandContext<ServerCommandSource>, transformers: Map<String, ArgumentTransformer<*, *, *>>): CommandContext {
        val transformedArguments = mutableMapOf<String, Any?>()
        for ((name, transformer) in transformers)
            transformedArguments[name] = transformArgument(context, name, transformer)
        return CommandContextImpl(context, transformedArguments)
    }

    private fun <T, A : ArgumentType<T>, B> transformArgument(context: com.mojang.brigadier.context.CommandContext<ServerCommandSource>, name: String, transformer: ArgumentTransformer<T, A, B>): T {
        val originalValue = context.getArgument(name, transformer.brigadierClass)
        val transformedValue = transformer.transform(context, originalValue)
        return transformedValue
    }

    private fun getBrigadierType(type: ArgumentType<*>): com.mojang.brigadier.arguments.ArgumentType<*> {
        return ArgumentTransformers.getBrigadierType(registryAccess, type)
    }
}