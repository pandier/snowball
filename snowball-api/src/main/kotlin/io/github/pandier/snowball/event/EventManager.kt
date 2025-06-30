package io.github.pandier.snowball.event

import java.util.function.Consumer

interface EventManager {
    fun registerListeners(vararg listeners: Any)
    fun registerListeners(listeners: Iterable<Any>)
    fun <T : Event> registerListener(eventType: Class<T>, listener: Consumer<T>)
    fun notify(event: Event)
}
