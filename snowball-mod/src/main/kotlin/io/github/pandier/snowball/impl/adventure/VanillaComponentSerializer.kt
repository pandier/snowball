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

private val namedTextColorSerialize = mapOf(
    NamedTextColor.BLACK to ChatFormatting.BLACK,
    NamedTextColor.DARK_BLUE to ChatFormatting.DARK_BLUE,
    NamedTextColor.DARK_GREEN to ChatFormatting.DARK_GREEN,
    NamedTextColor.DARK_AQUA to ChatFormatting.DARK_AQUA,
    NamedTextColor.DARK_RED to ChatFormatting.DARK_RED,
    NamedTextColor.DARK_PURPLE to ChatFormatting.DARK_PURPLE,
    NamedTextColor.GOLD to ChatFormatting.GOLD,
    NamedTextColor.GRAY to ChatFormatting.GRAY,
    NamedTextColor.DARK_GRAY to ChatFormatting.DARK_GRAY,
    NamedTextColor.BLUE to ChatFormatting.BLUE,
    NamedTextColor.GREEN to ChatFormatting.GREEN,
    NamedTextColor.AQUA to ChatFormatting.AQUA,
    NamedTextColor.RED to ChatFormatting.RED,
    NamedTextColor.LIGHT_PURPLE to ChatFormatting.LIGHT_PURPLE,
    NamedTextColor.YELLOW to ChatFormatting.YELLOW,
    NamedTextColor.WHITE to ChatFormatting.WHITE,
)

private val namedTextColorDeserialize = mapOf(
    ChatFormatting.BLACK to NamedTextColor.BLACK,
    ChatFormatting.DARK_BLUE to NamedTextColor.DARK_BLUE,
    ChatFormatting.DARK_GREEN to NamedTextColor.DARK_GREEN,
    ChatFormatting.DARK_AQUA to NamedTextColor.DARK_AQUA,
    ChatFormatting.DARK_RED to NamedTextColor.DARK_RED,
    ChatFormatting.DARK_PURPLE to NamedTextColor.DARK_PURPLE,
    ChatFormatting.GOLD to NamedTextColor.GOLD,
    ChatFormatting.GRAY to NamedTextColor.GRAY,
    ChatFormatting.DARK_GRAY to NamedTextColor.DARK_GRAY,
    ChatFormatting.BLUE to NamedTextColor.BLUE,
    ChatFormatting.GREEN to NamedTextColor.GREEN,
    ChatFormatting.AQUA to NamedTextColor.AQUA,
    ChatFormatting.RED to NamedTextColor.RED,
    ChatFormatting.LIGHT_PURPLE to NamedTextColor.LIGHT_PURPLE,
    ChatFormatting.YELLOW to NamedTextColor.YELLOW,
    ChatFormatting.WHITE to NamedTextColor.WHITE,
)

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
        return namedTextColorSerialize[color] ?: error("Unrecognizable named text color: $color")
    }

    fun deserialize(color: ChatFormatting): NamedTextColor {
        return namedTextColorDeserialize[color] ?: error("Unrecognizable color ChatFormatting: $color")
    }
}