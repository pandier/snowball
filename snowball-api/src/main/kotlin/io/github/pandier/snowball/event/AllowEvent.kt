package io.github.pandier.snowball.event

/**
 * An event that determines if a certain action should be allowed or not.
 */
public interface AllowEvent : Event {

    /**
     * Is true if the action is allowed.
     */
    public var allowed: Boolean

    /**
     * Denies the action.
     */
    public fun deny() {
        this.allowed = false
    }
}