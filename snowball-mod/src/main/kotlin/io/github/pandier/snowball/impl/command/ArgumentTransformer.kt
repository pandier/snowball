package io.github.pandier.snowball.impl.command

import io.github.pandier.snowball.command.type.ArgumentType
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.ServerCommandSource

class ArgumentTransformer<T, A : ArgumentType<T>, B>(
    val brigadierClass: Class<B>,
    private val brigadierTypeFactory: (CommandRegistryAccess, A) -> com.mojang.brigadier.arguments.ArgumentType<B>,
    private val transfomer: (com.mojang.brigadier.context.CommandContext<ServerCommandSource>, B) -> T,
) {
    fun brigadierType(registryAccess: CommandRegistryAccess, argument: A): com.mojang.brigadier.arguments.ArgumentType<B> = brigadierTypeFactory(registryAccess, argument)
    fun transform(context: com.mojang.brigadier.context.CommandContext<ServerCommandSource>, argument: B): T = transfomer(context, argument)
}
