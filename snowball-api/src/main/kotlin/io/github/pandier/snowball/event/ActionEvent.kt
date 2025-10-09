package io.github.pandier.snowball.event

/**
 * An event called before a certain action that can be prevented.
 */
public interface ActionEvent {

    /**
     * Is true if the default way of handling this action should be prevented.
     *
     * This should be used to modify the way the action is handled and **NOT** to cancel
     * the action entirely. Some form of action should still occur.
     * If you want to disallow any action, use [AllowEvent].
     */
    public var isDefaultPrevented: Boolean

    /**
     * Prevents the default way of handling this action
     *
     * This should be used to modify the way the action is handled and **NOT** to cancel
     * the action entirely. Some form of action should still occur.
     * If you want to disallow any action, use [AllowEvent].
     */
    public fun preventDefault() {
        this.isDefaultPrevented = true
    }
}