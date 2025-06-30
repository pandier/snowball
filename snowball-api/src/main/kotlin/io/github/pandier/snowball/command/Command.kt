package io.github.pandier.snowball.command

fun Command(name: String, block: Command.() -> Unit): Command {
    return Command(name).apply(block)
}

class Command(override val name: String) : CommandBranch(name)
