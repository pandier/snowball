package io.github.pandier.snowball.command

public fun interface CommandExecutor<S : CommandSource> {
    public fun CommandContext<S>.execute()
}
