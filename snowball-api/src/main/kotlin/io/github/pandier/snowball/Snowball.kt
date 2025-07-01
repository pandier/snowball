package io.github.pandier.snowball

import io.github.pandier.snowball.event.EventManager
import io.github.pandier.snowball.registry.SnowballRegistries
import io.github.pandier.snowball.server.Server

public interface Snowball {
    public companion object Holder {
        private lateinit var instance: Snowball

        @JvmStatic
        public fun get(): Snowball = instance
    }

    public val registries: SnowballRegistries
    public val eventManager: EventManager
    public val server: Server
}