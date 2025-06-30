package io.github.pandier.snowball.impl.plugin

import io.github.pandier.snowball.SnowballPlugin
import io.github.pandier.snowball.event.EventManager
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

private val logger: Logger = LogManager.getLogger()

class PluginManager(
    private val eventManager: EventManager,
) {
    var plugins: List<SnowballPlugin> = listOf()
        private set

    fun initialize() {
        plugins = FabricLoader.getInstance().getEntrypoints("snowball-plugin", SnowballPlugin::class.java)
        FabricLoader.getInstance().allMods

        try {
            eventManager.registerListeners(plugins)
        } catch (ex: Exception) {
            logger.error("Failed while registering plugins as listeners", ex)
        }
    }
}