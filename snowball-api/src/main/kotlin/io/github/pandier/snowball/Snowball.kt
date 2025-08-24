package io.github.pandier.snowball

import io.github.pandier.snowball.event.EventManager
import io.github.pandier.snowball.registry.SnowballRegistries
import io.github.pandier.snowball.server.Server

public object Snowball {
    private lateinit var instance: SnowballInterface

    @JvmStatic public val registries: SnowballRegistries get() = instance.registries
    @JvmStatic public val eventManager: EventManager get() = instance.eventManager
    @JvmStatic public val server: Server get() = instance.server
}

public interface SnowballInterface {
    public val registries: SnowballRegistries
    public val eventManager: EventManager
    public val server: Server
}
