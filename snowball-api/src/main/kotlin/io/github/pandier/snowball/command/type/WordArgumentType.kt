package io.github.pandier.snowball.command.type

class WordArgumentType : ArgumentType<String> {
    override val clazz: Class<String> = String::class.java
}