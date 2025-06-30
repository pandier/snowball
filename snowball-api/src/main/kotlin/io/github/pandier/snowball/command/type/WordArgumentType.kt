package io.github.pandier.snowball.command.type

public class WordArgumentType : ArgumentType<String> {
    override val clazz: Class<String> = String::class.java
}