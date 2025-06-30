package io.github.pandier.snowball.command

import io.github.pandier.snowball.command.type.ArgumentType

public open class CommandBranch(public open val name: String?) {
    public val arguments: MutableList<Argument<*>> = mutableListOf<Argument<*>>()
    public val children: MutableList<CommandBranch> = mutableListOf<CommandBranch>()
    public var executor: CommandExecutor<CommandSource>? = null

    public fun <T : Any> argument(type: ArgumentType<T>, name: String): ArgumentDelegate<T> {
        arguments.add(Argument(type, name))
        return ArgumentDelegate(name, type.clazz)
    }

    public fun branch(block: CommandBranch.() -> Unit) {
        branch(null, block)
    }

    public fun branch(name: String?, block: CommandBranch.() -> Unit) {
        add(CommandBranch(name).apply(block))
    }

    public fun add(branch: CommandBranch) {
        children.add(branch)
    }

    public fun executes(executor: CommandExecutor<CommandSource>) {
        this.executor = executor
    }
}