package io.github.pandier.snowball.command

interface CommandContext<S : CommandSource> {
    val source: S

    fun <T> require(name: String, clazz: Class<T>): T
    fun <T> get(name: String, clazz: Class<T>): T?
}