package io.github.pandier.snowball.impl.server

import io.github.pandier.snowball.server.Server
import net.minecraft.server.MinecraftServer

class ServerImpl(
    val base: MinecraftServer,
) : Server
