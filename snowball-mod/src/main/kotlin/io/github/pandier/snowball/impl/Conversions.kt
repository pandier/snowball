package io.github.pandier.snowball.impl

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.Player
import io.github.pandier.snowball.impl.adventure.AdventureText
import io.github.pandier.snowball.impl.adventure.VanillaComponentSerializer
import io.github.pandier.snowball.impl.bridge.SnowballConvertible
import io.github.pandier.snowball.impl.entity.EntityImpl
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.inventory.EquipmentSlot
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
import net.minecraft.block.Block
import net.minecraft.component.ComponentMap
import net.minecraft.component.ComponentType
import net.minecraft.item.Item
import net.minecraft.network.message.MessageType
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
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

    fun snowball(slot: net.minecraft.entity.EquipmentSlot): EquipmentSlot {
        return when (slot) {
            net.minecraft.entity.EquipmentSlot.MAINHAND -> EquipmentSlot.MAIN_HAND
            net.minecraft.entity.EquipmentSlot.OFFHAND -> EquipmentSlot.OFF_HAND
            net.minecraft.entity.EquipmentSlot.FEET -> EquipmentSlot.FEET
            net.minecraft.entity.EquipmentSlot.LEGS -> EquipmentSlot.LEGS
            net.minecraft.entity.EquipmentSlot.CHEST -> EquipmentSlot.CHEST
            net.minecraft.entity.EquipmentSlot.HEAD -> EquipmentSlot.HEAD
            net.minecraft.entity.EquipmentSlot.BODY -> EquipmentSlot.BODY
            net.minecraft.entity.EquipmentSlot.SADDLE -> EquipmentSlot.SADDLE
        }
    }

    fun vanilla(slot: EquipmentSlot): net.minecraft.entity.EquipmentSlot {
        return when (slot) {
            EquipmentSlot.MAIN_HAND -> net.minecraft.entity.EquipmentSlot.MAINHAND
            EquipmentSlot.OFF_HAND -> net.minecraft.entity.EquipmentSlot.OFFHAND
            EquipmentSlot.FEET -> net.minecraft.entity.EquipmentSlot.FEET
            EquipmentSlot.LEGS -> net.minecraft.entity.EquipmentSlot.LEGS
            EquipmentSlot.CHEST -> net.minecraft.entity.EquipmentSlot.CHEST
            EquipmentSlot.HEAD -> net.minecraft.entity.EquipmentSlot.HEAD
            EquipmentSlot.BODY -> net.minecraft.entity.EquipmentSlot.BODY
            EquipmentSlot.SADDLE -> net.minecraft.entity.EquipmentSlot.SADDLE
        }
    }

    fun snowball(type: ComponentType<*>): ItemComponentType<*> = SnowballImpl.registries.itemComponentType(type)

    fun snowball(stack: net.minecraft.item.ItemStack): ItemStack = ItemStackImpl(stack)
    fun snowball(inventory: net.minecraft.inventory.Inventory): Inventory = convertible(inventory)
    fun snowball(entity: net.minecraft.entity.Entity): Entity = convertible(entity)
    fun snowball(player: ServerPlayerEntity): Player = convertible(player)
    fun snowball(world: ServerWorld): World = convertible(world)
    fun snowball(block: Block): BlockType = convertible(block)
    fun snowball(state: net.minecraft.block.BlockState): BlockState = convertible(state)
    fun snowball(item: Item): ItemType = convertible(item)
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

        fun vanilla(component: Component): Text {
            return AdventureText(component)
        }

        fun adventure(text: Text): Component {
            return VanillaComponentSerializer.deserialize(text)
        }

        fun vanilla(color: NamedTextColor): Formatting {
            return VanillaComponentSerializer.serialize(color)
        }

        fun adventure(color: Formatting): NamedTextColor {
            return VanillaComponentSerializer.deserialize(color)
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