package io.github.pandier.snowball.command

public fun interface CommandExecutor {
    public fun CommandContext.execute(): CommandResult
}
