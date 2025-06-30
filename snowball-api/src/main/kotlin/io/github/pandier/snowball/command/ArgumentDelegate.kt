package io.github.pandier.snowball.command

import kotlin.reflect.KProperty

public class ArgumentDelegate<T>(
    private val name: String,
    private val clazz: Class<T>
) {
    public operator fun getValue(thisRef: Any?, property: KProperty<*>): CommandContext<*>.() -> T {
        return { require(name, clazz) }
    }
}
