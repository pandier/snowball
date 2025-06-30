package io.github.pandier.snowball.command

fun interface CommandExecutor<S : CommandSource> {
    fun CommandContext<S>.execute()
}
