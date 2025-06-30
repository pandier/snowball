package io.github.pandier.snowball.event

import java.util.function.Consumer

public interface EventManager {
    public fun registerListeners(vararg listeners: Any)
    public fun registerListeners(listeners: Iterable<Any>)
    public fun <T : Event> registerListener(eventType: Class<T>, listener: Consumer<T>)
    public fun notify(event: Event)
}
