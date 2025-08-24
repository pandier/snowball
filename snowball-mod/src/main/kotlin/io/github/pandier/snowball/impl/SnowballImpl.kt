package io.github.pandier.snowball.impl

import io.github.pandier.snowball.SnowballInterface
import io.github.pandier.snowball.event.EventManager
import io.github.pandier.snowball.impl.event.EventManagerImpl
import io.github.pandier.snowball.impl.plugin.PluginManager
import io.github.pandier.snowball.impl.registry.SnowballRegistriesImpl
import io.github.pandier.snowball.impl.server.ServerImpl
import io.github.pandier.snowball.registry.SnowballRegistries

object SnowballImpl : SnowballInterface {
    override val registries: SnowballRegistries = SnowballRegistriesImpl
    override val eventManager: EventManager = EventManagerImpl()
    override lateinit var server: ServerImpl
        internal set
    val pluginManager: PluginManager = PluginManager(eventManager)
}
