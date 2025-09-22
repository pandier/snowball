package io.github.pandier.snowball.impl.fabric

import io.github.pandier.snowball.event.player.PlayerJoinEvent
import io.github.pandier.snowball.event.server.CommandRegisterEvent
import io.github.pandier.snowball.event.server.ServerStartedEvent
import io.github.pandier.snowball.event.server.ServerStartingEvent
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.SnowballImpl
import io.github.pandier.snowball.impl.bridge.ServerPlayerEntityBridge
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
            SnowballImpl.eventManager.dispatch(ServerStartingEvent(server))
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            SnowballImpl.eventManager.dispatch(ServerStartedEvent(Conversions.snowball(it)))
        }

        ServerTickEvents.START_SERVER_TICK.register {
            SnowballImpl.server.scheduler.tick()
        }

        // ============= COMMAND =============

        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, _ ->
            val transformer = CommandTransformer(registryAccess)
            SnowballImpl.eventManager.dispatch(CommandRegisterEvent {
                dispatcher.register(transformer.transform(it))
            })
        }

        // ============= PLAYER =============

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