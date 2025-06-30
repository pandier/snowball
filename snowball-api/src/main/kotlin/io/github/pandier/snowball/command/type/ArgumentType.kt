package io.github.pandier.snowball.command.type

interface ArgumentType<T> {
    val clazz: Class<T>
}
