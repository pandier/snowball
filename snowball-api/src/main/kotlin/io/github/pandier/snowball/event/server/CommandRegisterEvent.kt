package io.github.pandier.snowball.event.server

import io.github.pandier.snowball.command.Command
import io.github.pandier.snowball.event.Event

class CommandRegisterEvent(
    private val registration: (Command) -> Unit
) : Event {
    fun register(command: Command) {
        registration(command)
    }
}
