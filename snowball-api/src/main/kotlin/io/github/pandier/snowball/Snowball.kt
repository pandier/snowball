package io.github.pandier.snowball

import io.github.pandier.snowball.event.EventManager

public interface Snowball {
    public companion object Holder {
        private lateinit var instance: Snowball

        @JvmStatic
        public fun get(): Snowball = instance
    }

    public val eventManager: EventManager
}