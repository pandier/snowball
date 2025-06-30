package io.github.pandier.snowball.command

public fun Command(name: String, block: Command.() -> Unit): Command {
    return Command(name).apply(block)
}

public class Command(override val name: String) : CommandBranch(name)
