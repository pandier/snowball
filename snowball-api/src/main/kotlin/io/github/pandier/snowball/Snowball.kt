package io.github.pandier.snowball

import io.github.pandier.snowball.event.EventManager
import io.github.pandier.snowball.factory.SnowballFactories
import io.github.pandier.snowball.registry.SnowballRegistries
import io.github.pandier.snowball.server.Server
import org.jetbrains.annotations.ApiStatus

public object Snowball {
    private lateinit var instance: SnowballInterface

    @JvmStatic public val registries: SnowballRegistries get() = instance.registries
    @JvmStatic public val factories: SnowballFactories get() = instance.factories
    @JvmStatic public val eventManager: EventManager get() = instance.eventManager
    @JvmStatic public val server: Server get() = instance.server
}

@ApiStatus.Internal
public interface SnowballInterface {
    public val registries: SnowballRegistries
    public val factories: SnowballFactories
    public val eventManager: EventManager
    public val server: Server
}
