package io.github.pandier.snowball.impl.adventure

import com.mojang.serialization.JsonOps
import io.github.pandier.snowball.impl.SnowballImpl
import net.kyori.adventure.text.*
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.RegistryOps

object VanillaComponentSerializer : ComponentSerializer<Component, Component, net.minecraft.network.chat.Component> {
    private val gsonSerializer = GsonComponentSerializer.gson()

    override fun serialize(component: Component): net.minecraft.network.chat.Component {
        val json = gsonSerializer.serializeToTree(component)
        val ops = RegistryOps.create(JsonOps.INSTANCE, SnowballImpl.server.adaptee.registryAccess())
        return ComponentSerialization.CODEC
            .decode(ops, json)
            .getOrThrow { error("Failed to serialize component: $it") }
            .first
    }

    override fun deserialize(input: net.minecraft.network.chat.Component): Component {
        if (input is AdventureText) return input.adventure

        val ops = RegistryOps.create(JsonOps.INSTANCE, SnowballImpl.server.adaptee.registryAccess())
        val json = ComponentSerialization.CODEC
            .encodeStart(ops, input)
            .getOrThrow { error("Failed to deserialize component: $it") }
        return gsonSerializer.deserializeFromTree(json)
    }

    fun serialize(color: NamedTextColor): ChatFormatting {
        return when (color) {
            NamedTextColor.BLACK -> ChatFormatting.BLACK
            NamedTextColor.DARK_BLUE -> ChatFormatting.DARK_BLUE
            NamedTextColor.DARK_GREEN -> ChatFormatting.DARK_GREEN
            NamedTextColor.DARK_AQUA -> ChatFormatting.DARK_AQUA
            NamedTextColor.DARK_RED -> ChatFormatting.DARK_RED
            NamedTextColor.DARK_PURPLE -> ChatFormatting.DARK_PURPLE
            NamedTextColor.GOLD -> ChatFormatting.GOLD
            NamedTextColor.GRAY -> ChatFormatting.GRAY
            NamedTextColor.DARK_GRAY -> ChatFormatting.DARK_GRAY
            NamedTextColor.BLUE -> ChatFormatting.BLUE
            NamedTextColor.GREEN -> ChatFormatting.GREEN
            NamedTextColor.AQUA -> ChatFormatting.AQUA
            NamedTextColor.RED -> ChatFormatting.RED
            NamedTextColor.LIGHT_PURPLE -> ChatFormatting.LIGHT_PURPLE
            NamedTextColor.YELLOW -> ChatFormatting.YELLOW
            NamedTextColor.WHITE -> ChatFormatting.WHITE
            else -> error("Unrecognizable named text color: $color")
        }
    }

    fun deserialize(color: ChatFormatting): NamedTextColor {
        return deserializeOrNull(color) ?: error("Unrecognizable color ChatFormatting: $color")
    }

    fun deserializeOrNull(color: ChatFormatting): NamedTextColor? {
        return when (color) {
            ChatFormatting.BLACK -> NamedTextColor.BLACK
            ChatFormatting.DARK_BLUE -> NamedTextColor.DARK_BLUE
            ChatFormatting.DARK_GREEN -> NamedTextColor.DARK_GREEN
            ChatFormatting.DARK_AQUA -> NamedTextColor.DARK_AQUA
            ChatFormatting.DARK_RED -> NamedTextColor.DARK_RED
            ChatFormatting.DARK_PURPLE -> NamedTextColor.DARK_PURPLE
            ChatFormatting.GOLD -> NamedTextColor.GOLD
            ChatFormatting.GRAY -> NamedTextColor.GRAY
            ChatFormatting.DARK_GRAY -> NamedTextColor.DARK_GRAY
            ChatFormatting.BLUE -> NamedTextColor.BLUE
            ChatFormatting.GREEN -> NamedTextColor.GREEN
            ChatFormatting.AQUA -> NamedTextColor.AQUA
            ChatFormatting.RED -> NamedTextColor.RED
            ChatFormatting.LIGHT_PURPLE -> NamedTextColor.LIGHT_PURPLE
            ChatFormatting.YELLOW -> NamedTextColor.YELLOW
            ChatFormatting.WHITE -> NamedTextColor.WHITE
            else -> null
        }
    }
}