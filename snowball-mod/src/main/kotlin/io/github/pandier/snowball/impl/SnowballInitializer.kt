package io.github.pandier.snowball.impl

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.event.player.PlayerJoinEvent
import io.github.pandier.snowball.event.server.CommandRegisterEvent
import io.github.pandier.snowball.event.server.ServerStartedEvent
import io.github.pandier.snowball.event.server.ServerStartingEvent
import io.github.pandier.snowball.impl.bridge.ServerPlayerEntityBridge
import io.github.pandier.snowball.impl.command.CommandTransformer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
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
            val server = Conversions.snowball(it)
            SnowballImpl.server = server
            SnowballImpl.eventManager.dispatch(ServerStartingEvent(server))
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            SnowballImpl.eventManager.dispatch(ServerStartedEvent(Conversions.snowball(it)))
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, _ ->
            val transformer = CommandTransformer(registryAccess)
            SnowballImpl.eventManager.dispatch(CommandRegisterEvent {
                dispatcher.register(transformer.transform(it))
            })
        }

        ServerPlayerEvents.JOIN.register { vPlayer ->
            val player = Conversions.snowball(vPlayer)

            val originalMessage = (vPlayer as ServerPlayerEntityBridge).`snowball$getJoinMessage`()
                ?.let(Conversions.Adventure::adventure)
            (vPlayer as ServerPlayerEntityBridge).`snowball$setJoinMessage`(null)

            val event = PlayerJoinEvent(player, originalMessage, SnowballImpl.server)
            SnowballImpl.eventManager.dispatch(event)

            event.message?.let { message ->
                event.audience.sendMessage(message)
            }
        }
    }
}