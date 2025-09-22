package io.github.pandier.snowball.event

/**
 * Event manager that is responsible for registering listeners and dispatching events.
 * When an event is dispatched, all listeners that are registered for that event will be called
 * with the provided event object.
 *
 * ### Inheritance
 *
 * Inheritance of events is taken into account when dispatching events. That means when a listener
 * is registered for a specific type of event, it will also be called for any subtype of that event.
 *
 * ### Registering and unregistering listeners
 *
 * Currently, the only way to register a listener is using the [Listener] annotation on a method which takes
 * an event object as its only parameter, and registering the object that contains the method with [register].
 * The [unregister] method can then be used to unregister all listeners of that container object.
 *
 * ```kt
 * class MyAmazingListeners {
 *     @Listener
 *     fun onPlayerJoin(event: PlayerJoinEvent) {
 *         // ...
 *     }
 * }
 *
 * // In your main class
 * Snowball.get().eventManager.register(MyAmazingListeners())
 * ```
 */
public interface EventManager {

    /**
     * Registers all methods in the provided [container] object that are annotated with [Listener] as listeners.
     * Does nothing if the container is already registered.
     *
     * @throws IllegalStateException if a method is annotated with [Listener] but is not a valid listener method
     */
    public fun register(container: Any)

    /**
     * Unregisters all listeners associated with the provided [container] object.
     * Does nothing if the container is not registered.
     */
    public fun unregister(container: Any)

    /**
     * Dispatches the provided [event] to all registered listeners of the event's type and all its super types.
     * Exceptions are handled by the event manager and are not propagated to the caller.
     */
    public fun dispatch(event: Event)
}
