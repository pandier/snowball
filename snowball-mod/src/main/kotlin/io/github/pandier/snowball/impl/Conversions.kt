package io.github.pandier.snowball.impl

import io.github.pandier.snowball.entity.Attribute
import io.github.pandier.snowball.entity.AttributeModifier
import io.github.pandier.snowball.entity.AttributeType
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.EntityType
import io.github.pandier.snowball.entity.player.GameMode
import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.impl.adventure.AdventureText
import io.github.pandier.snowball.impl.adventure.VanillaComponentSerializer
import io.github.pandier.snowball.impl.bridge.SnowballConvertible
import io.github.pandier.snowball.impl.entity.EntityImpl
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.entity.EquipmentSlot
import io.github.pandier.snowball.entity.ItemEntity
import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.entity.damage.DamageType
import io.github.pandier.snowball.entity.player.Hand
import io.github.pandier.snowball.impl.entity.AttributeImpl
import io.github.pandier.snowball.impl.entity.damage.DamageSourceImpl
import io.github.pandier.snowball.impl.inventory.InventoryImpl
import io.github.pandier.snowball.inventory.Inventory
import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemRarity
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.profile.GameProfile
import io.github.pandier.snowball.profile.GameProfileProperty
import io.github.pandier.snowball.server.Server
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockState
import io.github.pandier.snowball.world.block.BlockType
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.util.Ticks
import net.minecraft.ChatFormatting
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.Container
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.GameType
import net.minecraft.world.level.block.Block
import java.time.Duration
import java.util.Optional

object Conversions {
    fun snowball(gameMode: GameType): GameMode = when (gameMode) {
        GameType.SURVIVAL -> GameMode.SURVIVAL
        GameType.CREATIVE -> GameMode.CREATIVE
        GameType.ADVENTURE -> GameMode.ADVENTURE
        GameType.SPECTATOR -> GameMode.SPECTATOR
    }

    fun vanilla(gameMode: GameMode): GameType = when (gameMode) {
        GameMode.SURVIVAL -> GameType.SURVIVAL
        GameMode.CREATIVE -> GameType.CREATIVE
        GameMode.ADVENTURE -> GameType.ADVENTURE
        GameMode.SPECTATOR -> GameType.SPECTATOR
    }

    fun snowball(hand: InteractionHand): Hand = when (hand) {
        InteractionHand.MAIN_HAND -> Hand.MAIN
        InteractionHand.OFF_HAND -> Hand.OFF
    }

    fun vanilla(hand: Hand): InteractionHand = when (hand) {
        Hand.MAIN -> InteractionHand.MAIN_HAND
        Hand.OFF -> InteractionHand.OFF_HAND
    }

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

    fun snowball(rarity: Rarity): ItemRarity {
        return when (rarity) {
            Rarity.COMMON -> ItemRarity.COMMON
            Rarity.UNCOMMON -> ItemRarity.UNCOMMON
            Rarity.RARE -> ItemRarity.RARE
            Rarity.EPIC -> ItemRarity.EPIC
        }
    }

    fun vanilla(rarity: ItemRarity): Rarity {
        return when (rarity) {
            ItemRarity.COMMON -> Rarity.COMMON
            ItemRarity.UNCOMMON -> Rarity.UNCOMMON
            ItemRarity.RARE -> Rarity.RARE
            ItemRarity.EPIC -> Rarity.EPIC
        }
    }

    fun snowball(slot: net.minecraft.world.entity.EquipmentSlot): EquipmentSlot {
        return when (slot) {
            net.minecraft.world.entity.EquipmentSlot.MAINHAND -> EquipmentSlot.MAIN_HAND
            net.minecraft.world.entity.EquipmentSlot.OFFHAND -> EquipmentSlot.OFF_HAND
            net.minecraft.world.entity.EquipmentSlot.FEET -> EquipmentSlot.FEET
            net.minecraft.world.entity.EquipmentSlot.LEGS -> EquipmentSlot.LEGS
            net.minecraft.world.entity.EquipmentSlot.CHEST -> EquipmentSlot.CHEST
            net.minecraft.world.entity.EquipmentSlot.HEAD -> EquipmentSlot.HEAD
            net.minecraft.world.entity.EquipmentSlot.BODY -> EquipmentSlot.BODY
            net.minecraft.world.entity.EquipmentSlot.SADDLE -> EquipmentSlot.SADDLE
        }
    }

    fun vanilla(slot: EquipmentSlot): net.minecraft.world.entity.EquipmentSlot {
        return when (slot) {
            EquipmentSlot.MAIN_HAND -> net.minecraft.world.entity.EquipmentSlot.MAINHAND
            EquipmentSlot.OFF_HAND -> net.minecraft.world.entity.EquipmentSlot.OFFHAND
            EquipmentSlot.FEET -> net.minecraft.world.entity.EquipmentSlot.FEET
            EquipmentSlot.LEGS -> net.minecraft.world.entity.EquipmentSlot.LEGS
            EquipmentSlot.CHEST -> net.minecraft.world.entity.EquipmentSlot.CHEST
            EquipmentSlot.HEAD -> net.minecraft.world.entity.EquipmentSlot.HEAD
            EquipmentSlot.BODY -> net.minecraft.world.entity.EquipmentSlot.BODY
            EquipmentSlot.SADDLE -> net.minecraft.world.entity.EquipmentSlot.SADDLE
        }
    }

    fun snowball(modifier: net.minecraft.world.entity.ai.attributes.AttributeModifier): AttributeModifier =
        AttributeModifier(Adventure.adventure(modifier.id), modifier.amount, snowball(modifier.operation))

    fun vanilla(modifier: AttributeModifier): net.minecraft.world.entity.ai.attributes.AttributeModifier =
        net.minecraft.world.entity.ai.attributes.AttributeModifier(Adventure.vanilla(modifier.key), modifier.amount, vanilla(modifier.operation))

    fun snowball(operation: net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation): AttributeModifier.Operation {
        return when (operation) {
            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE -> AttributeModifier.Operation.ADD_VALUE
            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE -> AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL -> AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        }
    }

    fun vanilla(operation: AttributeModifier.Operation): net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation {
        return when (operation) {
            AttributeModifier.Operation.ADD_VALUE -> net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE -> net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL -> net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        }
    }

    fun snowball(type: DataComponentType<*>): ItemComponentType<*> = SnowballImpl.registries.itemComponentType(type)
    fun snowball(stack: net.minecraft.world.item.ItemStack): ItemStack = ItemStackImpl(stack)
    fun snowball(container: Container): Inventory = InventoryImpl(container)
    fun snowball(source: net.minecraft.world.damagesource.DamageSource): DamageSource = DamageSourceImpl(source)
    fun snowball(type: net.minecraft.world.damagesource.DamageType): DamageType = convertible(type)
    fun snowball(entityType: net.minecraft.world.entity.EntityType<*>): EntityType<*> = convertible(entityType)
    fun snowball(entity: net.minecraft.world.entity.Entity): Entity = convertible(entity)
    fun snowball(entity: net.minecraft.world.entity.LivingEntity): LivingEntity = convertible(entity)
    fun snowball(entity: net.minecraft.world.entity.item.ItemEntity): ItemEntity = convertible(entity)
    fun snowball(player: ServerPlayer): Player = convertible(player)
    fun snowball(attribute: net.minecraft.world.entity.ai.attributes.Attribute): AttributeType = convertible(attribute)
    fun snowball(instance: AttributeInstance): Attribute = AttributeImpl(instance)
    fun snowball(level: ServerLevel): World = convertible(level)
    fun snowball(block: Block): BlockType = convertible(block)
    fun snowball(state: net.minecraft.world.level.block.state.BlockState): BlockState = convertible(state)
    fun snowball(item: Item): ItemType = convertible(item)
    fun snowball(obj: MinecraftServer): Server = convertible(obj)

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> convertible(any: Any): T = (any as SnowballConvertible<T>).`snowball$get`()

    object Adventure {
        fun vanilla(key: Key): Identifier {
            return Identifier.fromNamespaceAndPath(key.namespace(), key.value())
        }

        fun adventure(identifier: Identifier): Key {
            return Key.key(identifier.namespace, identifier.path)
        }

        inline fun <reified T : Any> resourceKey(registry: ResourceKey<out Registry<T>>, key: Key): ResourceKey<T> {
            return ResourceKey.create(registry, vanilla(key))
        }

        fun toTicks(duration: Duration): Long {
            return duration.toMillis() / Ticks.SINGLE_TICK_DURATION_MS
        }

        fun vanilla(component: Component): net.minecraft.network.chat.Component {
            return AdventureText(component)
        }

        fun adventure(text: net.minecraft.network.chat.Component): Component {
            return VanillaComponentSerializer.deserialize(text)
        }

        fun vanilla(color: NamedTextColor): ChatFormatting {
            return VanillaComponentSerializer.serialize(color)
        }

        fun adventure(color: ChatFormatting): NamedTextColor {
            return VanillaComponentSerializer.deserialize(color)
        }

        fun vanilla(bound: ChatType.Bound, registryAccess: RegistryAccess): net.minecraft.network.chat.ChatType.Bound {
            return net.minecraft.network.chat.ChatType.Bound(
                registryAccess.getOrThrow(resourceKey(Registries.CHAT_TYPE, bound.type().key())),
                vanilla(bound.name()),
                Optional.ofNullable(bound.target()?.let(::vanilla)),
            )
        }

        fun vanilla(source: Sound.Source): SoundSource {
            return when (source) {
                Sound.Source.MASTER -> SoundSource.MASTER
                Sound.Source.MUSIC -> SoundSource.MUSIC
                Sound.Source.RECORD -> SoundSource.RECORDS
                Sound.Source.WEATHER -> SoundSource.WEATHER
                Sound.Source.BLOCK -> SoundSource.BLOCKS
                Sound.Source.HOSTILE -> SoundSource.HOSTILE
                Sound.Source.NEUTRAL -> SoundSource.NEUTRAL
                Sound.Source.PLAYER -> SoundSource.PLAYERS
                Sound.Source.AMBIENT -> SoundSource.AMBIENT
                Sound.Source.VOICE -> SoundSource.VOICE
                Sound.Source.UI -> SoundSource.UI
            }
        }

        fun vanilla(emitter: Sound.Emitter, self: net.minecraft.world.entity.Entity): net.minecraft.world.entity.Entity {
            if (emitter == Sound.Emitter.self()) return self
            if (emitter is EntityImpl) return emitter.adaptee
            error("Unrecognizable emitter: ${emitter.javaClass}")
        }
    }
}