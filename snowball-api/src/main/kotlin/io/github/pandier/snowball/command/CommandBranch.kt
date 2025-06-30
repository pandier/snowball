package io.github.pandier.snowball.command

import io.github.pandier.snowball.command.type.ArgumentType

open class CommandBranch(open val name: String?) {
    val arguments = mutableListOf<Argument<*>>()
    val children = mutableListOf<CommandBranch>()
    var executor: CommandExecutor<CommandSource>? = null

    fun <T : Any> argument(type: ArgumentType<T>, name: String): ArgumentDelegate<T> {
        arguments.add(Argument(type, name))
        return ArgumentDelegate(name, type.clazz)
    }

    fun <T : Any> argumentOptional(type: ArgumentType<T>, name: String): OptionalArgumentDelegate<T> {
        arguments.add(Argument(type, name))
        return OptionalArgumentDelegate(name, type.clazz)
    }

    fun branch(block: CommandBranch.() -> Unit) {
        branch(null, block)
    }

    fun branch(name: String?, block: CommandBranch.() -> Unit) {
        add(CommandBranch(name).apply(block))
    }

    fun add(branch: CommandBranch) {
        children.add(branch)
    }

    fun executes(executor: CommandExecutor<CommandSource>) {
        this.executor = executor
    }
}