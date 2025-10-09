package io.github.pandier.snowball.event

/**
 * An event that determines if a certain action should be allowed or not.
 */
public interface AllowEvent : Event {
    public var isAllowed: Boolean

    public fun deny() {
        this.isAllowed = false
    }
}