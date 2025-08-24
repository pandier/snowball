package io.github.pandier.snowball.impl.event

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import io.github.pandier.snowball.event.Event
import io.github.pandier.snowball.event.EventManager
import io.github.pandier.snowball.event.Listener
import org.apache.logging.log4j.LogManager
import java.util.function.Consumer

private val logger = LogManager.getLogger()

class EventManagerImpl : EventManager {
    private val containers: Multimap<Any, RegisteredListener<out Event>> = HashMultimap.create()
    private val listeners: Multimap<Class<out Event>, RegisteredListener<out Event>> = HashMultimap.create()

    override fun register(container: Any) {
        if (containers.containsKey(container)) return

        for (method in container.javaClass.declaredMethods) {
            if (!method.isAnnotationPresent(Listener::class.java)) continue
            try {
                val parameterTypes = method.parameterTypes
                if (parameterTypes.size != 1 || !Event::class.java.isAssignableFrom(parameterTypes[0]))
                    throw IllegalStateException("Invalid parameters (${parameterTypes.joinToString(", ") { it.name }})")

                @Suppress("UNCHECKED_CAST")
                val eventType = parameterTypes[0] as Class<out Event>
                try {
                    method.isAccessible = true
                } catch (ex: Exception) {
                    throw IllegalStateException("Failed to make method accessible", ex)
                }

                val registeredListener = RegisteredListener(eventType) {
                    method.invoke(container, it)
                }

                containers.put(container, registeredListener)
                listeners.put(eventType, registeredListener)
            } catch (ex: Exception) {
                throw IllegalStateException("Couldn't register method '${method.name}' of ${container.javaClass.name} as listener", ex)
            }
        }
    }

    override fun unregister(container: Any) {
        val containerListeners = containers.removeAll(container)
        for (listener in containerListeners) {
            listeners.remove(listener.eventType, listener)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun dispatch(event: Event) {
        // TODO: Cache supertypes
        val iterator = SuperTypesIterator(event.javaClass)
        while (iterator.hasNext()) {
            val type = iterator.next()
            val listeners = (listeners[type] ?: continue) as Collection<RegisteredListener<Event>>
            for (listener in listeners) {
                try {
                    listener.executor.accept(event)
                } catch (ex: Exception) {
                    // TODO: Log the plugin's name?
                    logger.error("An error occurred while dispatching event ${event.javaClass.name} to listener", ex)
                }
            }
        }
    }

    private class RegisteredListener<T : Event>(
        val eventType: Class<T>,
        val executor: Consumer<in T>,
    )

    private class SuperTypesIterator(type: Class<out Event>) : Iterator<Class<out Event>> {
        private val types = ArrayDeque<Class<out Event>>()
        private val visited = mutableSetOf<Class<*>>()

        init {
            types.add(type)
        }

        override fun hasNext(): Boolean =
            types.isNotEmpty()

        @Suppress("UNCHECKED_CAST")
        override fun next(): Class<out Event> {
            val type = types.removeLast()

            val superclass = type.superclass
            if (superclass != null && Event::class.java.isAssignableFrom(superclass))
                types.add(superclass as Class<out Event>) // Superclasses are guaranteed to be unvisited

            for (interfaceType in type.interfaces) {
                if (Event::class.java.isAssignableFrom(interfaceType) && visited.add(interfaceType)) {
                    types.add(interfaceType as Class<out Event>)
                }
            }

            return type
        }
    }
}
