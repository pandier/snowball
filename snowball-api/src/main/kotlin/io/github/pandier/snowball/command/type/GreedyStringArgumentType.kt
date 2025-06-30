package io.github.pandier.snowball.command.type

public class GreedyStringArgumentType : ArgumentType<String> {
    override val clazz: Class<String> = String::class.java
}