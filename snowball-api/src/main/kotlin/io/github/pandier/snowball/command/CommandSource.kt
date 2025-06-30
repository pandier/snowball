package io.github.pandier.snowball.command

import net.kyori.adventure.text.Component

interface CommandSource {
    fun sendMessage(message: Component)
}
