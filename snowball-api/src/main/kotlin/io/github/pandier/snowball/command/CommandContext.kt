package io.github.pandier.snowball.command

public interface CommandContext<S : CommandSource> {
    public val source: S

    public fun <T> require(name: String, clazz: Class<T>): T
    public fun <T> get(name: String, clazz: Class<T>): T?
}