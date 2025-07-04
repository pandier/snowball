package io.github.pandier.snowball.impl

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.bridge.SnowballConvertible
import io.github.pandier.snowball.impl.entity.EntityImpl
import io.github.pandier.snowball.impl.mixin.StyleAccessor
import io.github.pandier.snowball.impl.mixin.TextColorAccessor
import io.github.pandier.snowball.profile.GameProfile
import io.github.pandier.snowball.profile.GameProfileProperty
import io.github.pandier.snowball.server.Server
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockState
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
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
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.util.Ticks
import net.minecraft.block.Block
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.network.message.FilterMask
import net.minecraft.network.message.LastSeenMessageList
import net.minecraft.network.message.MessageBody
import net.minecraft.network.message.MessageLink
import net.minecraft.network.message.MessageSignatureData
import net.minecraft.network.message.MessageType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.text.BlockNbtDataSource
import net.minecraft.text.EntityNbtDataSource
import net.minecraft.text.KeybindTextContent
import net.minecraft.text.NbtTextContent
import net.minecraft.text.ParsedSelector
import net.minecraft.text.PlainTextContent
import net.minecraft.text.ScoreTextContent
import net.minecraft.text.SelectorTextContent
import net.minecraft.text.StorageNbtDataSource
import net.minecraft.text.Text
import net.minecraft.text.TranslatableTextContent
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.time.Duration
import java.util.Optional

object Conversions {
    fun snowball(gameProfile: com.mojang.authlib.GameProfile): GameProfile =
        GameProfile(gameProfile.id, gameProfile.name, gameProfile.properties.values().map(::snowball))

    fun vanilla(gameProfile: GameProfile): com.mojang.authlib.GameProfile {
        return com.mojang.authlib.GameProfile(gameProfile.uuid, gameProfile.name).apply {
            for (property in gameProfile.properties) {
                properties.put(property.name, vanilla(property))
            }
        }
    }

    fun snowball(property: com.mojang.authlib.properties.Property): GameProfileProperty =
        GameProfileProperty(property.name, property.value, property.signature)

    fun vanilla(property: GameProfileProperty): com.mojang.authlib.properties.Property =
        com.mojang.authlib.properties.Property(property.name, property.value, property.signature)

    fun snowball(entity: net.minecraft.entity.Entity): Entity = convertible(entity)
    fun snowball(player: ServerPlayerEntity): Player = convertible(player)
    fun snowball(world: ServerWorld): World = convertible(world)
    fun snowball(block: Block): BlockType = convertible(block)
    fun snowball(state: net.minecraft.block.BlockState): BlockState = convertible(state)
    fun snowball(obj: MinecraftServer): Server = convertible(obj)

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> convertible(any: Any): T = (any as SnowballConvertible<T>).`snowball$get`()

    object Adventure {
        fun vanilla(key: Key): Identifier {
            return Identifier.of(key.namespace(), key.value())
        }

        fun adventure(identifier: Identifier): Key {
            return Key.key(identifier.namespace, identifier.path)
        }

        inline fun <reified T> registryKey(registry: RegistryKey<out Registry<T>>, key: Key): RegistryKey<T> {
            return RegistryKey.of(registry, vanilla(key))
        }

        fun toTicks(duration: Duration): Long {
            return duration.toMillis() / Ticks.SINGLE_TICK_DURATION_MS
        }

        fun vanilla(color: NamedTextColor): Formatting {
            return when (color) {
                NamedTextColor.BLACK -> Formatting.BLACK
                NamedTextColor.DARK_BLUE -> Formatting.DARK_BLUE
                NamedTextColor.DARK_GREEN -> Formatting.DARK_GREEN
                NamedTextColor.DARK_AQUA -> Formatting.DARK_AQUA
                NamedTextColor.DARK_RED -> Formatting.DARK_RED
                NamedTextColor.DARK_PURPLE -> Formatting.DARK_PURPLE
                NamedTextColor.GOLD -> Formatting.GOLD
                NamedTextColor.GRAY -> Formatting.GRAY
                NamedTextColor.DARK_GRAY -> Formatting.DARK_GRAY
                NamedTextColor.BLUE -> Formatting.BLUE
                NamedTextColor.GREEN -> Formatting.GREEN
                NamedTextColor.AQUA -> Formatting.AQUA
                NamedTextColor.RED -> Formatting.RED
                NamedTextColor.LIGHT_PURPLE -> Formatting.LIGHT_PURPLE
                NamedTextColor.YELLOW -> Formatting.YELLOW
                NamedTextColor.WHITE -> Formatting.WHITE
                else -> error("Unrecognizable named text color: $color")
            }
        }

        fun adventure(color: Formatting): NamedTextColor {
            return when (color) {
                Formatting.BLACK -> NamedTextColor.BLACK
                Formatting.DARK_BLUE -> NamedTextColor.DARK_BLUE
                Formatting.DARK_GREEN -> NamedTextColor.DARK_GREEN
                Formatting.DARK_AQUA -> NamedTextColor.DARK_AQUA
                Formatting.DARK_RED -> NamedTextColor.DARK_RED
                Formatting.DARK_PURPLE -> NamedTextColor.DARK_PURPLE
                Formatting.GOLD -> NamedTextColor.GOLD
                Formatting.GRAY -> NamedTextColor.GRAY
                Formatting.DARK_GRAY -> NamedTextColor.DARK_GRAY
                Formatting.BLUE -> NamedTextColor.BLUE
                Formatting.GREEN -> NamedTextColor.GREEN
                Formatting.AQUA -> NamedTextColor.AQUA
                Formatting.RED -> NamedTextColor.RED
                Formatting.LIGHT_PURPLE -> NamedTextColor.LIGHT_PURPLE
                Formatting.YELLOW -> NamedTextColor.YELLOW
                Formatting.WHITE -> NamedTextColor.WHITE
                else -> error("Unrecognizable color formatting: $color")
            }
        }

        fun vanilla(color: TextColor): net.minecraft.text.TextColor {
            if (color is NamedTextColor) return net.minecraft.text.TextColor.fromFormatting(vanilla(color))!!
            return net.minecraft.text.TextColor.fromRgb(color.value())
        }

        @Suppress("CAST_NEVER_SUCCEEDS")
        fun adventure(color: net.minecraft.text.TextColor): TextColor {
            val name = (color as TextColorAccessor).`snowball$getName`()
            if (name != null) return adventure(Formatting.byName(name) ?: error("Unrecognizable formatting name: $name"))
            return TextColor.color(color.rgb)
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

        fun adventure(event: net.minecraft.text.ClickEvent): ClickEvent {
            return when (event) {
                is net.minecraft.text.ClickEvent.OpenUrl -> ClickEvent.openUrl(event.uri.toString())
                is net.minecraft.text.ClickEvent.OpenFile -> ClickEvent.openFile(event.path)
                is net.minecraft.text.ClickEvent.RunCommand -> ClickEvent.runCommand(event.command)
                is net.minecraft.text.ClickEvent.SuggestCommand -> ClickEvent.suggestCommand(event.command)
                is net.minecraft.text.ClickEvent.ChangePage -> ClickEvent.changePage(event.page)
                is net.minecraft.text.ClickEvent.CopyToClipboard -> ClickEvent.copyToClipboard(event.value)
                // TODO
                is net.minecraft.text.ClickEvent.ShowDialog -> error("SHOW_DIALOG click event action is not supported yet")
                is net.minecraft.text.ClickEvent.Custom -> ClickEvent.custom(adventure(event.id), event.payload.flatMap(NbtElement::asString).orElse(""))
                else -> error("Unrecognizable click event: ${event.javaClass}")
            }
        }

        fun vanilla(event: HoverEvent<*>): net.minecraft.text.HoverEvent {
            return when (event.action()) {
                HoverEvent.Action.SHOW_TEXT -> net.minecraft.text.HoverEvent.ShowText(vanilla(event.value() as Component))
                // TODO
                else -> error("${event.action()} hover event is not supported yet")
            }
        }

        fun adventure(event: net.minecraft.text.HoverEvent): HoverEvent<*> {
            return when (event) {
                is net.minecraft.text.HoverEvent.ShowText -> HoverEvent.showText(adventure(event.value))
                // TODO
                else -> error("${event.action} hover event is not supported yet")
            }
        }

        fun vanilla(style: Style): net.minecraft.text.Style {
            if (style === Style.empty()) return net.minecraft.text.Style.EMPTY
            return StyleAccessor.`snowball$init`(
                style.color()?.let(::vanilla),
                style.shadowColor()?.value(),
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

        fun adventure(style: net.minecraft.text.Style): Style {
            if (style == net.minecraft.text.Style.EMPTY) return Style.empty()
            val accessor = style as StyleAccessor
            return Style.style()
                .color(style.color?.let(::adventure))
                .shadowColor(style.shadowColor?.let { ShadowColor.shadowColor(it) })
                .decoration(TextDecoration.BOLD, TextDecoration.State.byBoolean(accessor.`snowball$getBold`()))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.byBoolean(accessor.`snowball$getItalic`()))
                .decoration(TextDecoration.UNDERLINED, TextDecoration.State.byBoolean(accessor.`snowball$getUnderlined`()))
                .decoration(TextDecoration.STRIKETHROUGH, TextDecoration.State.byBoolean(accessor.`snowball$getStrikethrough`()))
                .decoration(TextDecoration.OBFUSCATED, TextDecoration.State.byBoolean(accessor.`snowball$getObfuscated`()))
                .clickEvent(style.clickEvent?.let(::adventure))
                .hoverEvent(style.hoverEvent?.let(::adventure))
                .insertion(style.insertion)
                .font(style.font?.let(::adventure))
                .build()
        }

        fun vanilla(component: Component): Text {
            val vanilla = when (component) {
                is TextComponent -> Text.literal(component.content())
                is TranslatableComponent -> Text.translatableWithFallback(component.key(), component.fallback(),
                    component.arguments().map { vanilla(it.asComponent()) })
                is KeybindComponent -> Text.keybind(component.keybind())
                is ScoreComponent -> Text.score(component.name(), component.objective())
                is SelectorComponent -> Text.selector(
                    ParsedSelector.parse(component.pattern()).getOrThrow(),
                    Optional.ofNullable(component.separator()?.let(::vanilla)))
                is BlockNBTComponent -> Text.nbt(component.nbtPath(), component.interpret(),
                    Optional.ofNullable(component.separator()?.let(::vanilla)),
                    BlockNbtDataSource(component.pos().asString())
                )
                is EntityNBTComponent -> Text.nbt(component.nbtPath(), component.interpret(),
                    Optional.ofNullable(component.separator()?.let(::vanilla)),
                    EntityNbtDataSource(component.selector())
                )
                is StorageNBTComponent -> Text.nbt(component.nbtPath(), component.interpret(),
                    Optional.ofNullable(component.separator()?.let(::vanilla)),
                    StorageNbtDataSource(vanilla(component.storage()))
                )
                else -> error("Unrecognizable component class: ${component.javaClass}")
            }
            for (child in component.children())
                vanilla.append(vanilla(child))
            vanilla.style = vanilla(component.style())
            return vanilla
        }

        fun adventure(text: Text): Component {
            val content = text.content
            val adventure = when (content) {
                is PlainTextContent -> {
                    if (content == PlainTextContent.EMPTY) {
                        Component.empty().toBuilder()
                    } else {
                        Component.text().content(content.string())
                    }
                }
                is TranslatableTextContent -> Component.translatable()
                    .key(content.key)
                    .arguments(content.args.map { if (it is Text) adventure(it) else Component.text(it.toString()) })
                is KeybindTextContent ->
                    Component.keybind().keybind(content.key)
                is ScoreTextContent ->
                    Component.score()
                        .name(content.name.map({ it.comp_3067 }, { it }))
                        .objective(content.objective)
                is SelectorTextContent ->
                    Component.selector()
                        .pattern(content.selector.comp_3067)
                        .separator(content.separator.orElse(null)?.let(Adventure::adventure))
                is NbtTextContent -> {
                    val dataSource = content.dataSource
                    val adventure = when (dataSource) {
                        is BlockNbtDataSource -> Component.blockNBT().pos(BlockNBTComponent.Pos.fromString(dataSource.rawPos))
                        is EntityNbtDataSource -> Component.entityNBT().selector(dataSource.rawSelector)
                        is StorageNbtDataSource -> Component.storageNBT().storage(adventure(dataSource.id))
                        else -> error("Unrecognizable NBT data source: ${dataSource.javaClass}")
                    }
                    adventure
                        .nbtPath(content.path)
                        .interpret(content.shouldInterpret())
                        .separator(content.separator.orElse(null)?.let(Adventure::adventure))
                }
                else -> error("Unrecognizable text content: ${content.javaClass}")
            }
            for (child in text.siblings)
                adventure.append(adventure(child))
            adventure.style(adventure(text.style))
            return adventure.build()
        }

        fun vanilla(signedMessage: SignedMessage): net.minecraft.network.message.SignedMessage {
            return net.minecraft.network.message.SignedMessage(
                MessageLink.of(signedMessage.identity().uuid()),
                signedMessage.signature()?.let { MessageSignatureData(it.bytes()) },
                MessageBody(
                    signedMessage.message(),
                    signedMessage.timestamp(),
                    signedMessage.salt(),
                    LastSeenMessageList.EMPTY
                ),
                signedMessage.unsignedContent()?.let(::vanilla),
                FilterMask.PASS_THROUGH
            )
        }

        fun vanilla(bound: ChatType.Bound, registryManager: DynamicRegistryManager): MessageType.Parameters {
            return MessageType.Parameters(
                registryManager.getEntryOrThrow(registryKey(RegistryKeys.MESSAGE_TYPE, bound.type().key())),
                vanilla(bound.name()),
                Optional.ofNullable(bound.target()?.let(::vanilla)),
            )
        }

        fun vanilla(source: Sound.Source): SoundCategory {
            return when (source) {
                Sound.Source.MASTER -> SoundCategory.MASTER
                Sound.Source.MUSIC -> SoundCategory.MUSIC
                Sound.Source.RECORD -> SoundCategory.RECORDS
                Sound.Source.WEATHER -> SoundCategory.WEATHER
                Sound.Source.BLOCK -> SoundCategory.BLOCKS
                Sound.Source.HOSTILE -> SoundCategory.HOSTILE
                Sound.Source.NEUTRAL -> SoundCategory.NEUTRAL
                Sound.Source.PLAYER -> SoundCategory.PLAYERS
                Sound.Source.AMBIENT -> SoundCategory.AMBIENT
                Sound.Source.VOICE -> SoundCategory.VOICE
                Sound.Source.UI -> SoundCategory.UI
            }
        }

        fun vanilla(emitter: Sound.Emitter, self: net.minecraft.entity.Entity): net.minecraft.entity.Entity {
            if (emitter == Sound.Emitter.self()) return self
            if (emitter is EntityImpl) return emitter.adaptee
            error("Unrecognizable emitter: ${emitter.javaClass}")
        }
    }
}