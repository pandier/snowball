package io.github.pandier.snowball.impl

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.event.server.ServerStartedEvent
import io.github.pandier.snowball.event.server.ServerStartingEvent
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

class SnowballInitializer : ModInitializer {

    override fun onInitialize() {
        injectInstance()
        registerListeners()
        SnowballImpl.pluginManager.initialize()
    }

    @Suppress("JAVA_CLASS_ON_COMPANION")
    private fun injectInstance() {
        try {
            val instanceField = Snowball.Holder::class.java.getDeclaredField("instance")
            instanceField.isAccessible = true
            instanceField.set(Snowball.Holder, SnowballImpl)
        } catch (ex: Exception) {
            throw IllegalStateException("Failed to inject Snowball instance into holder", ex)
        }
    }

    private fun registerListeners() {
        ServerLifecycleEvents.SERVER_STARTING.register {
            SnowballImpl.eventManager.notify(ServerStartingEvent(Conversions.server(it)))
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            SnowballImpl.eventManager.notify(ServerStartedEvent(Conversions.server(it)))
        }
    }
}