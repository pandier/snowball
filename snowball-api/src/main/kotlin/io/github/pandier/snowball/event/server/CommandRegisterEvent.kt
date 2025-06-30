package io.github.pandier.snowball.event.server

import io.github.pandier.snowball.command.Command
import io.github.pandier.snowball.event.Event

public class CommandRegisterEvent(
    private val registration: (Command) -> Unit
) : Event {
    public fun register(command: Command) {
        registration(command)
    }
}
