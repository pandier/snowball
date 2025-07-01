package io.github.pandier.snowball.impl

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.event.EventManager
import io.github.pandier.snowball.impl.event.EventManagerImpl
import io.github.pandier.snowball.impl.plugin.PluginManager
import io.github.pandier.snowball.server.Server

object SnowballImpl : Snowball {
    override val eventManager: EventManager = EventManagerImpl()
    override lateinit var server: Server
        internal set
    val pluginManager: PluginManager = PluginManager(eventManager)
}
