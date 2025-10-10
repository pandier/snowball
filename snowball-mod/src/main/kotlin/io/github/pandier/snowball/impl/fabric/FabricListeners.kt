package io.github.pandier.snowball.impl.fabric

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.event.entity.player.PlayerJoinEvent
import io.github.pandier.snowball.event.server.CommandRegisterEvent
import io.github.pandier.snowball.event.server.ServerStartedEvent
import io.github.pandier.snowball.event.server.ServerStartingEvent
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.SnowballImpl
import io.github.pandier.snowball.impl.bridge.ServerPlayerBridge
import io.github.pandier.snowball.impl.bridge.SnowballConvertible
import io.github.pandier.snowball.impl.command.CommandTransformer
import io.github.pandier.snowball.impl.server.ServerImpl
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

object FabricListeners {

    fun register() {
        // ============= SERVER =============

        ServerLifecycleEvents.SERVER_STARTING.register {
            @Suppress("UNCHECKED_CAST")
            val server = (it as SnowballConvertible<ServerImpl>).`snowball$get`()
            SnowballImpl.server = server
            Snowball.eventManager.dispatch(ServerStartingEvent(server))
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            Snowball.eventManager.dispatch(ServerStartedEvent(Conversions.snowball(it)))
        }

        ServerTickEvents.START_SERVER_TICK.register {
            SnowballImpl.server.scheduler.tick()
        }

        // ============= COMMAND =============

        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, _ ->
            val transformer = CommandTransformer(registryAccess)
            Snowball.eventManager.dispatch(CommandRegisterEvent {
                dispatcher.register(transformer.transform(it))
            })
        }

        // ============= PLAYER =============

        ServerPlayerEvents.JOIN.register { vPlayer ->
            val player = Conversions.snowball(vPlayer)

            val originalMessage = (vPlayer as ServerPlayerBridge).`snowball$getJoinMessage`()
                ?.let(Conversions.Adventure::adventure)
            (vPlayer as ServerPlayerBridge).`snowball$setJoinMessage`(null)

            val event = PlayerJoinEvent(player, originalMessage, SnowballImpl.server)
            Snowball.eventManager.dispatch(event)

            event.message?.let { message ->
                event.audience.sendMessage(message)
            }
        }
    }
}