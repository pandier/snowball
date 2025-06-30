package io.github.pandier.snowball.command.type

class BooleanArgumentType : ArgumentType<Boolean> {
    override val clazz: Class<Boolean> = Boolean::class.java
}
