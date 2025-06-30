package io.github.pandier.snowball.command.type

class StringArgumentType : ArgumentType<String> {
    override val clazz: Class<String> = String::class.java
}