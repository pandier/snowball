package io.github.pandier.snowball.impl

import io.github.pandier.snowball.impl.bridge.SnowballConvertible
import io.github.pandier.snowball.impl.mixin.StyleAccessor
import io.github.pandier.snowball.server.Server
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.BlockNBTComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.EntityNBTComponent
import net.kyori.adventure.text.KeybindComponent
import net.kyori.adventure.text.ScoreComponent
import net.kyori.adventure.text.SelectorComponent
import net.kyori.adventure.text.StorageNBTComponent
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.nbt.NbtString
import net.minecraft.server.MinecraftServer
import net.minecraft.text.BlockNbtDataSource
import net.minecraft.text.EntityNbtDataSource
import net.minecraft.text.ParsedSelector
import net.minecraft.text.StorageNbtDataSource
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.util.Optional

object Conversions {
    fun server(obj: MinecraftServer): Server = convertible(obj)

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> convertible(any: Any): T = (any as SnowballConvertible<T>).`snowball$get`()

    object Adventure {
        fun vanilla(key: Key): Identifier {
            return Identifier.of(key.namespace(), key.value())
        }

        fun vanilla(color: TextColor): net.minecraft.text.TextColor {
            return net.minecraft.text.TextColor.fromRgb(color.value())
        }

        fun vanilla(shadowColor: ShadowColor): Int {
            return shadowColor.value()
        }

        fun vanilla(state: TextDecoration.State): Boolean? {
            return when (state) {
                TextDecoration.State.NOT_SET -> null
                TextDecoration.State.TRUE -> true
                TextDecoration.State.FALSE -> false
            }
        }

        fun vanilla(event: ClickEvent): net.minecraft.text.ClickEvent {
            return when (event.action()) {
                ClickEvent.Action.OPEN_URL -> net.minecraft.text.ClickEvent.OpenUrl(Util.validateUri((event.payload() as ClickEvent.Payload.Text).value()))
                ClickEvent.Action.OPEN_FILE -> net.minecraft.text.ClickEvent.OpenFile((event.payload() as ClickEvent.Payload.Text).value())
                ClickEvent.Action.RUN_COMMAND -> net.minecraft.text.ClickEvent.RunCommand((event.payload() as ClickEvent.Payload.Text).value())
                ClickEvent.Action.SUGGEST_COMMAND -> net.minecraft.text.ClickEvent.SuggestCommand((event.payload() as ClickEvent.Payload.Text).value())
                ClickEvent.Action.CHANGE_PAGE -> net.minecraft.text.ClickEvent.ChangePage((event.payload() as ClickEvent.Payload.Int).integer())
                ClickEvent.Action.COPY_TO_CLIPBOARD -> net.minecraft.text.ClickEvent.CopyToClipboard((event.payload() as ClickEvent.Payload.Text).value())
                // TODO: this will be painful ;-;
                ClickEvent.Action.SHOW_DIALOG -> error("SHOW_DIALOG click event action is not supported yet")
                ClickEvent.Action.CUSTOM -> {
                    val payload = event.payload() as ClickEvent.Payload.Custom
                    net.minecraft.text.ClickEvent.Custom(vanilla(payload.key()), Optional.of(NbtString.of(payload.data())))
                }
            }
        }

        fun vanilla(event: HoverEvent<*>): net.minecraft.text.HoverEvent {
            return when (event.action()) {
                HoverEvent.Action.SHOW_TEXT -> net.minecraft.text.HoverEvent.ShowText(vanilla(event.value() as Component))
                // TODO
                else -> error("${event.action()} hover event is not supported yet")
            }
        }

        fun vanilla(style: Style): net.minecraft.text.Style {
            if (style === Style.empty()) return net.minecraft.text.Style.EMPTY
            return StyleAccessor.init(
                style.color()?.let(::vanilla),
                style.shadowColor()?.let(::vanilla),
                style.decoration(TextDecoration.BOLD).let(::vanilla),
                style.decoration(TextDecoration.ITALIC).let(::vanilla),
                style.decoration(TextDecoration.UNDERLINED).let(::vanilla),
                style.decoration(TextDecoration.STRIKETHROUGH).let(::vanilla),
                style.decoration(TextDecoration.OBFUSCATED).let(::vanilla),
                style.clickEvent()?.let(::vanilla),
                style.hoverEvent()?.let(::vanilla),
                style.insertion(),
                style.font()?.let(::vanilla),
            )
        }

        fun vanilla(component: Component): Text {
            val vanilla = when (component) {
                is TextComponent -> Text.literal(component.content())
                is TranslatableComponent -> Text.translatableWithFallback(component.key(), component.fallback(),
                    component.arguments().map { vanilla(it.asComponent()) })
                is KeybindComponent -> Text.keybind(component.keybind())
                is ScoreComponent -> Text.score(component.name(), component.objective())
                is SelectorComponent -> Text.selector(ParsedSelector.parse(component.pattern()).getOrThrow(),
                    Optional.ofNullable(component.separator()?.let(::vanilla)))
                is BlockNBTComponent -> Text.nbt(component.nbtPath(), component.interpret(),
                    Optional.ofNullable(component.separator()?.let(::vanilla)),
                    BlockNbtDataSource(component.pos().asString()))
                is EntityNBTComponent -> Text.nbt(component.nbtPath(), component.interpret(),
                    Optional.ofNullable(component.separator()?.let(::vanilla)),
                    EntityNbtDataSource(component.selector()))
                is StorageNBTComponent -> Text.nbt(component.nbtPath(), component.interpret(),
                    Optional.ofNullable(component.separator()?.let(::vanilla)),
                    StorageNbtDataSource(vanilla(component.storage())))
                else -> error("Unrecognizable component class: ${component.javaClass.name}")
            }
            for (child in component.children())
                vanilla.append(vanilla(child))
            vanilla.style = vanilla(component.style())
            return vanilla
        }
    }
}