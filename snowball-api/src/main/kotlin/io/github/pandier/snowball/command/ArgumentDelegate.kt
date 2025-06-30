package io.github.pandier.snowball.command

import kotlin.reflect.KProperty

class ArgumentDelegate<T>(private val name: String, private val clazz: Class<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): CommandContext<*>.() -> T {
        return { require(name, clazz) }
    }
}
