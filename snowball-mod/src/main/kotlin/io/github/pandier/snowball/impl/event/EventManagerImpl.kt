package io.github.pandier.snowball.impl.event

import io.github.pandier.snowball.event.Event
import io.github.pandier.snowball.event.EventManager
import io.github.pandier.snowball.event.Listener
import java.util.function.Consumer

class EventManagerImpl : EventManager {
    private val listeners: MutableMap<Class<out Event>, MutableList<Consumer<out Event>>> = mutableMapOf()

    override fun registerListeners(vararg listeners: Any) {
        for (listener in listeners) {
            registerListenerMethods(listener)
        }
    }

    override fun registerListeners(listeners: Iterable<Any>) {
        for (listener in listeners) {
            registerListenerMethods(listener)
        }
    }

    private fun registerListenerMethods(o: Any) {
        for (method in o.javaClass.declaredMethods) {
            if (!method.isAnnotationPresent(Listener::class.java)) continue
            try {
                val parameterTypes = method.parameterTypes
                if (parameterTypes.size != 1 || !Event::class.java.isAssignableFrom(parameterTypes[0]))
                    error("Invalid parameters (${parameterTypes.joinToString(", ") { it.name }})")
                val eventType = parameterTypes[0]
                try {
                    method.isAccessible = true
                } catch (ex: Exception) {
                    throw IllegalStateException("Failed to make method accessible", ex)
                }
                @Suppress("UNCHECKED_CAST")
                registerListener(eventType as Class<out Event>) {
                    method.invoke(o, it)
                }
            } catch (ex: Exception) {
                throw IllegalStateException("Couldn't register method '${method.name}' of ${o.javaClass.name} as listener", ex)
            }
        }
    }

    override fun <T : Event> registerListener(
        eventType: Class<T>,
        listener: Consumer<T>
    ) {
        val eventListeners = listeners.computeIfAbsent(eventType) { mutableListOf() }
        eventListeners.add(listener)
    }

    override fun notify(event: Event) {
        notifyInternal(event)
    }

    private fun <T : Event> notifyInternal(event: T) {
        val eventType = event.javaClass
        for (listener in getEventListeners(eventType)) {
            listener.accept(event)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Event> getEventListeners(eventType: Class<T>): List<Consumer<T>> {
        return (listeners[eventType] ?: listOf()) as List<Consumer<T>>
    }
}
