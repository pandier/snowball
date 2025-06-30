package io.github.pandier.snowball.impl

import io.github.pandier.snowball.impl.bridge.SnowballConvertible
import io.github.pandier.snowball.server.Server
import net.minecraft.server.MinecraftServer

object Conversions {
    fun server(obj: MinecraftServer): Server = convertible(obj)

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> convertible(any: Any): T = (any as SnowballConvertible<T>).`snowball$get`()
}