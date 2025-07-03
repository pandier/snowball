package io.github.pandier.snowball.command

import io.github.pandier.snowball.command.type.ArgumentType

public open class CommandBranch(public open val name: String?) {
    public val arguments: MutableList<Argument<*>> = mutableListOf()
    public val children: MutableList<CommandBranch> = mutableListOf()
    public var executor: CommandExecutor? = null

    public fun <T : Any> argument(type: ArgumentType<T>, name: String): ArgumentDelegate<T> {
        arguments.add(Argument(type, name))
        return ArgumentDelegate(name)
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

    public fun executes(executor: CommandExecutor) {
        this.executor = executor
    }
}