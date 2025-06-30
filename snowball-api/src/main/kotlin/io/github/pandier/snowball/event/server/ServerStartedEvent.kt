package io.github.pandier.snowball.event.server

import io.github.pandier.snowball.event.Event
import io.github.pandier.snowball.server.Server

class ServerStartedEvent(
    val server: Server
) : Event
