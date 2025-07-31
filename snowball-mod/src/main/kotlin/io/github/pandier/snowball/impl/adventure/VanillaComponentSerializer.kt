package io.github.pandier.snowball.impl.adventure

import io.github.pandier.snowball.impl.Conversions.Adventure
import io.github.pandier.snowball.impl.mixin.StyleAccessor
import io.github.pandier.snowball.impl.mixin.TextColorAccessor
import net.kyori.adventure.text.*
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.*
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.registry.Registries
import net.minecraft.text.*
import net.minecraft.util.Formatting
import net.minecraft.util.Util
import java.net.URISyntaxException
import java.util.*

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

    override fun serialize(component: Component): Text {
        val vanilla = when (component) {
            is TextComponent -> Text.literal(component.content())
            is TranslatableComponent -> Text.translatableWithFallback(component.key(), component.fallback(),
                component.arguments().map { serialize(it.asComponent()) })
            is KeybindComponent -> Text.keybind(component.keybind())
            is ScoreComponent -> Text.score(component.name(), component.objective())
            is SelectorComponent -> Text.selector(
                ParsedSelector.parse(component.pattern()).getOrThrow(),
                Optional.ofNullable(component.separator()?.let(::serialize)))
            is BlockNBTComponent -> Text.nbt(component.nbtPath(), component.interpret(),
                Optional.ofNullable(component.separator()?.let(::serialize)),
                BlockNbtDataSource(component.pos().asString())
            )
            is EntityNBTComponent -> Text.nbt(component.nbtPath(), component.interpret(),
                Optional.ofNullable(component.separator()?.let(::serialize)),
                EntityNbtDataSource(component.selector())
            )
            is StorageNBTComponent -> Text.nbt(component.nbtPath(), component.interpret(),
                Optional.ofNullable(component.separator()?.let(::serialize)),
                StorageNbtDataSource(Adventure.vanilla(component.storage()))
            )
            else -> error("Unrecognizable component class: ${component.javaClass}")
        }

        for (child in component.children())
            vanilla.append(serialize(child))

        vanilla.style = serialize(component.style())
        return vanilla
    }

    override fun deserialize(input: Text): Component {
        if (input is AdventureText) return input.adventure

        val adventure = when (val content = input.content) {
            is PlainTextContent -> Component.text().content(content.string())
            is TranslatableTextContent -> Component.translatable()
                .key(content.key)
                .arguments(content.args.map { if (it is Text) deserialize(it) else Component.text(it.toString()) })
            is KeybindTextContent -> Component.keybind().keybind(content.key)
            is ScoreTextContent -> Component.score()
                .name(content.name.map({ it.comp_3067 }, { it }))
                .objective(content.objective)
            is SelectorTextContent -> Component.selector()
                .pattern(content.selector.comp_3067)
                .separator(content.separator.orElse(null)?.let(Adventure::adventure))
            is NbtTextContent -> {
                val adventure = when (val dataSource = content.dataSource) {
                    is BlockNbtDataSource -> Component.blockNBT().pos(BlockNBTComponent.Pos.fromString(dataSource.rawPos))
                    is EntityNbtDataSource -> Component.entityNBT().selector(dataSource.rawSelector)
                    is StorageNbtDataSource -> Component.storageNBT().storage(Adventure.adventure(dataSource.id))
                    else -> error("Unrecognizable NBT data source: ${dataSource.javaClass}")
                }
                adventure
                    .nbtPath(content.path)
                    .interpret(content.shouldInterpret())
                    .separator(content.separator.orElse(null)?.let(Adventure::adventure))
            }
            else -> error("Unrecognizable text content: ${content.javaClass}")
        }

        for (child in input.siblings)
            adventure.append(deserialize(child))

        adventure.style(deserialize(input.style))
        return adventure.build()
    }

    fun serialize(style: Style): net.minecraft.text.Style {
        if (style === Style.empty()) return net.minecraft.text.Style.EMPTY
        return StyleAccessor.`snowball$init`(
            style.color()?.let(::serialize),
            style.shadowColor()?.value(),
            style.decoration(TextDecoration.BOLD).let(::toBooleanNullable),
            style.decoration(TextDecoration.ITALIC).let(::toBooleanNullable),
            style.decoration(TextDecoration.UNDERLINED).let(::toBooleanNullable),
            style.decoration(TextDecoration.STRIKETHROUGH).let(::toBooleanNullable),
            style.decoration(TextDecoration.OBFUSCATED).let(::toBooleanNullable),
            style.clickEvent()?.let(::serialize),
            style.hoverEvent()?.let(::serialize),
            style.insertion(),
            style.font()?.let(Adventure::vanilla),
        )
    }

    fun deserialize(style: net.minecraft.text.Style): Style {
        if (style === net.minecraft.text.Style.EMPTY) return Style.empty()
        val accessor = style as StyleAccessor
        return Style.style()
            .color(style.color?.let(::deserialize))
            .shadowColor(style.shadowColor?.let { ShadowColor.shadowColor(it) })
            .decoration(TextDecoration.BOLD, TextDecoration.State.byBoolean(accessor.`snowball$getBold`()))
            .decoration(TextDecoration.ITALIC, TextDecoration.State.byBoolean(accessor.`snowball$getItalic`()))
            .decoration(TextDecoration.UNDERLINED, TextDecoration.State.byBoolean(accessor.`snowball$getUnderlined`()))
            .decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.byBoolean(accessor.`snowball$getStrikethrough`()))
            .decoration(TextDecoration.OBFUSCATED, TextDecoration.State.byBoolean(accessor.`snowball$getObfuscated`()))
            .clickEvent(style.clickEvent?.let(::deserialize))
            .hoverEvent(style.hoverEvent?.let(::deserialize))
            .insertion(style.insertion)
            .font(style.font?.let(Adventure::adventure))
            .build()
    }

    private fun serialize(color: NamedTextColor): Formatting {
        return namedTextColorSerialize[color] ?: error("Unrecognizable named text color: $color")
    }

    private fun deserialize(color: Formatting): NamedTextColor {
        return namedTextColorDeserialize[color] ?: error("Unrecognizable color formatting: $color")
    }

    fun serialize(color: TextColor): net.minecraft.text.TextColor {
        if (color is NamedTextColor) return net.minecraft.text.TextColor.fromFormatting(serialize(color))!!
        return net.minecraft.text.TextColor.fromRgb(color.value())
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    fun deserialize(color: net.minecraft.text.TextColor): TextColor {
        val name = (color as TextColorAccessor).`snowball$getName`()
        if (name != null) return deserialize(Formatting.byName(name) ?: error("Unrecognizable formatting name: $name"))
        return TextColor.color(color.rgb)
    }

    fun serialize(event: ClickEvent): net.minecraft.text.ClickEvent {
        return when (event.action()) {
            ClickEvent.Action.OPEN_URL -> {
                net.minecraft.text.ClickEvent.OpenUrl(try {
                    Util.validateUri((event.payload() as ClickEvent.Payload.Text).value())
                } catch (ex: URISyntaxException) {
                    throw IllegalArgumentException(ex.message, ex)
                })
            }
            ClickEvent.Action.OPEN_FILE -> net.minecraft.text.ClickEvent.OpenFile((event.payload() as ClickEvent.Payload.Text).value())
            ClickEvent.Action.RUN_COMMAND -> net.minecraft.text.ClickEvent.RunCommand((event.payload() as ClickEvent.Payload.Text).value())
            ClickEvent.Action.SUGGEST_COMMAND -> net.minecraft.text.ClickEvent.SuggestCommand((event.payload() as ClickEvent.Payload.Text).value())
            ClickEvent.Action.CHANGE_PAGE -> net.minecraft.text.ClickEvent.ChangePage((event.payload() as ClickEvent.Payload.Int).integer())
            ClickEvent.Action.COPY_TO_CLIPBOARD -> net.minecraft.text.ClickEvent.CopyToClipboard((event.payload() as ClickEvent.Payload.Text).value())
            ClickEvent.Action.SHOW_DIALOG -> error("SHOW_DIALOG click event action is not supported yet")
            ClickEvent.Action.CUSTOM -> {
                val payload = event.payload() as ClickEvent.Payload.Custom
                net.minecraft.text.ClickEvent.Custom(Adventure.vanilla(payload.key()), Optional.of(NbtString.of(payload.data())))
            }
        }
    }

    fun deserialize(event: net.minecraft.text.ClickEvent): ClickEvent {
        return when (event) {
            is net.minecraft.text.ClickEvent.OpenUrl -> ClickEvent.openUrl(event.uri.toString())
            is net.minecraft.text.ClickEvent.OpenFile -> ClickEvent.openFile(event.path)
            is net.minecraft.text.ClickEvent.RunCommand -> ClickEvent.runCommand(event.command)
            is net.minecraft.text.ClickEvent.SuggestCommand -> ClickEvent.suggestCommand(event.command)
            is net.minecraft.text.ClickEvent.ChangePage -> ClickEvent.changePage(event.page)
            is net.minecraft.text.ClickEvent.CopyToClipboard -> ClickEvent.copyToClipboard(event.value)
            is net.minecraft.text.ClickEvent.ShowDialog -> ClickEvent.showDialog(DummyDialog)
            is net.minecraft.text.ClickEvent.Custom -> ClickEvent.custom(Adventure.adventure(event.id), event.payload.flatMap(NbtElement::asString).orElse(""))
            else -> error("Unrecognizable click event: ${event.javaClass}")
        }
    }

    fun serialize(event: HoverEvent<*>): net.minecraft.text.HoverEvent {
        return when (event.action()) {
            HoverEvent.Action.SHOW_TEXT -> net.minecraft.text.HoverEvent.ShowText(serialize(event.value() as Component))
            HoverEvent.Action.SHOW_ITEM -> {
                val showItem = event.value() as HoverEvent.ShowItem
                net.minecraft.text.HoverEvent.ShowItem(ItemStack(
                    Registries.ITEM.get(Adventure.vanilla(showItem.item())),
                    showItem.count(),
                ))
            }
            HoverEvent.Action.SHOW_ENTITY -> {
                val showEntity = event.value() as HoverEvent.ShowEntity
                net.minecraft.text.HoverEvent.ShowEntity(net.minecraft.text.HoverEvent.EntityContent(
                    Registries.ENTITY_TYPE.get(Adventure.vanilla(showEntity.type())),
                    showEntity.id(),
                    showEntity.name()?.let(::serialize),
                ))
            }
            else -> error("${event.action()} hover event is not supported")
        }
    }

    fun deserialize(event: net.minecraft.text.HoverEvent): HoverEvent<*> {
        return when (event) {
            is net.minecraft.text.HoverEvent.ShowText -> HoverEvent.showText(deserialize(event.value))
            is net.minecraft.text.HoverEvent.ShowItem -> HoverEvent.showItem(
                Adventure.adventure(Registries.ITEM.getId(event.item.item)),
                event.item.count
            )
            is net.minecraft.text.HoverEvent.ShowEntity -> HoverEvent.showEntity(
                Adventure.adventure(Registries.ENTITY_TYPE.getId(event.entity.entityType)),
                event.entity.uuid,
                event.entity.name.orElse(null)?.let(::deserialize)
            )
            else -> error("${event.action} hover event is not supported")
        }
    }

    private fun toBooleanNullable(state: TextDecoration.State): Boolean? {
        return when (state) {
            TextDecoration.State.NOT_SET -> null
            TextDecoration.State.TRUE -> true
            TextDecoration.State.FALSE -> false
        }
    }
}