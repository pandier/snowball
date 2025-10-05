package io.github.pandier.snowball.impl.adventure

import com.mojang.serialization.JsonOps
import io.github.pandier.snowball.impl.SnowballImpl
import net.kyori.adventure.text.*
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.registry.RegistryOps
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs
import net.minecraft.util.Formatting

private val namedTextColorSerialize = mapOf(
    NamedTextColor.BLACK to Formatting.BLACK,
    NamedTextColor.DARK_BLUE to Formatting.DARK_BLUE,
    NamedTextColor.DARK_GREEN to Formatting.DARK_GREEN,
    NamedTextColor.DARK_AQUA to Formatting.DARK_AQUA,
    NamedTextColor.DARK_RED to Formatting.DARK_RED,
    NamedTextColor.DARK_PURPLE to Formatting.DARK_PURPLE,
    NamedTextColor.GOLD to Formatting.GOLD,
    NamedTextColor.GRAY to Formatting.GRAY,
    NamedTextColor.DARK_GRAY to Formatting.DARK_GRAY,
    NamedTextColor.BLUE to Formatting.BLUE,
    NamedTextColor.GREEN to Formatting.GREEN,
    NamedTextColor.AQUA to Formatting.AQUA,
    NamedTextColor.RED to Formatting.RED,
    NamedTextColor.LIGHT_PURPLE to Formatting.LIGHT_PURPLE,
    NamedTextColor.YELLOW to Formatting.YELLOW,
    NamedTextColor.WHITE to Formatting.WHITE,
)

private val namedTextColorDeserialize = mapOf(
    Formatting.BLACK to NamedTextColor.BLACK,
    Formatting.DARK_BLUE to NamedTextColor.DARK_BLUE,
    Formatting.DARK_GREEN to NamedTextColor.DARK_GREEN,
    Formatting.DARK_AQUA to NamedTextColor.DARK_AQUA,
    Formatting.DARK_RED to NamedTextColor.DARK_RED,
    Formatting.DARK_PURPLE to NamedTextColor.DARK_PURPLE,
    Formatting.GOLD to NamedTextColor.GOLD,
    Formatting.GRAY to NamedTextColor.GRAY,
    Formatting.DARK_GRAY to NamedTextColor.DARK_GRAY,
    Formatting.BLUE to NamedTextColor.BLUE,
    Formatting.GREEN to NamedTextColor.GREEN,
    Formatting.AQUA to NamedTextColor.AQUA,
    Formatting.RED to NamedTextColor.RED,
    Formatting.LIGHT_PURPLE to NamedTextColor.LIGHT_PURPLE,
    Formatting.YELLOW to NamedTextColor.YELLOW,
    Formatting.WHITE to NamedTextColor.WHITE,
)

object VanillaComponentSerializer : ComponentSerializer<Component, Component, Text> {
    private val gsonSerializer = GsonComponentSerializer.gson()

    override fun serialize(component: Component): Text {
        val json = gsonSerializer.serializeToTree(component)
        val ops = RegistryOps.of(JsonOps.INSTANCE, SnowballImpl.server.adaptee.registryManager)
        return TextCodecs.CODEC
            .decode(ops, json)
            .getOrThrow { error("Failed to serialize component: $it") }
            .first
    }

    override fun deserialize(input: Text): Component {
        if (input is AdventureText) return input.adventure

        val ops = RegistryOps.of(JsonOps.INSTANCE, SnowballImpl.server.adaptee.registryManager)
        val json = TextCodecs.CODEC.encodeStart(ops, input)
            .getOrThrow { error("Failed to deserialize component: $it") }
        return gsonSerializer.deserializeFromTree(json)
    }

    fun serialize(color: NamedTextColor): Formatting {
        return namedTextColorSerialize[color] ?: error("Unrecognizable named text color: $color")
    }

    fun deserialize(color: Formatting): NamedTextColor {
        return namedTextColorDeserialize[color] ?: error("Unrecognizable color formatting: $color")
    }
}