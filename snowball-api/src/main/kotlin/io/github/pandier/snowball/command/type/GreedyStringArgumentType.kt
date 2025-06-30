package io.github.pandier.snowball.command.type

class GreedyStringArgumentType : ArgumentType<String> {
    override val clazz: Class<String> = String::class.java
}