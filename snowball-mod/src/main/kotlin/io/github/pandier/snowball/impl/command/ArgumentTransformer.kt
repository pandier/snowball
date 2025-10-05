package io.github.pandier.snowball.impl.command

import io.github.pandier.snowball.command.type.ArgumentType
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack

class ArgumentTransformer<T, A : ArgumentType<T>, B>(
    val brigadierClass: Class<B>,
    private val brigadierTypeFactory: (CommandBuildContext, A) -> com.mojang.brigadier.arguments.ArgumentType<B>,
    private val transfomer: (com.mojang.brigadier.context.CommandContext<CommandSourceStack>, B) -> T,
) {
    fun brigadierType(registryAccess: CommandBuildContext, argument: A): com.mojang.brigadier.arguments.ArgumentType<B> = brigadierTypeFactory(registryAccess, argument)
    fun transform(context: com.mojang.brigadier.context.CommandContext<CommandSourceStack>, argument: B): T = transfomer(context, argument)
}
