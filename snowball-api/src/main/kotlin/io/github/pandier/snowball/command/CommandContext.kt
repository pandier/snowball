package io.github.pandier.snowball.command

public interface CommandContext {
    public val source: CommandSource

    public fun <T> require(name: String): T
    public fun <T> get(name: String): T?
}
