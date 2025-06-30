package io.github.pandier.snowball.command.type

public class StringArgumentType : ArgumentType<String> {
    override val clazz: Class<String> = String::class.java
}