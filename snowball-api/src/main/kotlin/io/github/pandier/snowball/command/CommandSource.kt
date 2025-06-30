package io.github.pandier.snowball.command

import net.kyori.adventure.text.Component

public interface CommandSource {
    public fun sendMessage(message: Component)
}
