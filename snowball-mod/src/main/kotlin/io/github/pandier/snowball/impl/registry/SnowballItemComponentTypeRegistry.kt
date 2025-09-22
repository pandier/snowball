package io.github.pandier.snowball.impl.registry

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.item.ItemComponentTypeImpl
import io.github.pandier.snowball.item.ItemComponentType
import io.github.pandier.snowball.item.ItemRarity
import io.github.pandier.snowball.util.Color
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.minecraft.component.ComponentType
import net.minecraft.component.type.BlockStateComponent
import net.minecraft.component.type.DyedColorComponent
import net.minecraft.component.type.LoreComponent
import net.minecraft.component.type.MapColorComponent
import net.minecraft.component.type.MapIdComponent
import net.minecraft.component.type.OminousBottleAmplifierComponent
import net.minecraft.component.type.WritableBookContentComponent
import net.minecraft.registry.Registries
import net.minecraft.text.RawFilteredPair
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import java.util.Collections
import java.util.Optional
import java.util.function.Function

/**
 * A registry that properly handles [io.github.pandier.snowball.item.ItemComponentType]s.
 */
class SnowballItemComponentTypeRegistry {
    private val entries = mutableMapOf<Key, ItemComponentType<*>>()

    fun registerDefaults(): SnowballItemComponentTypeRegistry {
        // TODO: Implement all unknowns
        registerMinecraftUnknown("custom_data")
        registerMinecraftDirect<Int>("max_stack_size")
        registerMinecraftDirect<Int>("max_damage")
        registerMinecraftDirect<Int>("damage")
        registerMinecraft<Unit, net.minecraft.util.Unit>("unbreakable", {}, { net.minecraft.util.Unit.INSTANCE })
        registerMinecraft<Component, Text>("custom_name", Conversions.Adventure::adventure, Conversions.Adventure::vanilla)
        registerMinecraft<Component, Text>("item_name", Conversions.Adventure::adventure, Conversions.Adventure::vanilla)
        registerMinecraft<Key, Identifier>("item_model", Conversions.Adventure::adventure, Conversions.Adventure::vanilla)
        registerMinecraft<List<Component>, LoreComponent>("lore", { it.lines.map(Conversions.Adventure::adventure) }, { LoreComponent(it.map(Conversions.Adventure::vanilla)) })
        registerMinecraft<ItemRarity, Rarity>("rarity", Conversions::snowball, Conversions::vanilla)
        registerMinecraftUnknown("enchantments")
        registerMinecraftUnknown("can_place_on")
        registerMinecraftUnknown("can_break")
        registerMinecraftUnknown("attribute_modifiers")
        registerMinecraftUnknown("custom_model_data")
        registerMinecraftUnknown("tooltip_display")
        registerMinecraftDirect<Int>("repair_cost")
        registerMinecraft<Unit, net.minecraft.util.Unit>("creative_slot_lock", {}, { net.minecraft.util.Unit.INSTANCE })
        registerMinecraftDirect<Boolean>("enchantment_glint_override")
        registerMinecraft<Unit, net.minecraft.util.Unit>("intangible_projectile", {}, { net.minecraft.util.Unit.INSTANCE })
        registerMinecraftUnknown("food")
        registerMinecraftUnknown("consumable")
        registerMinecraftUnknown("use_remainder")
        registerMinecraftUnknown("use_cooldown")
        registerMinecraftUnknown("damage_resistant")
        registerMinecraftUnknown("tool")
        registerMinecraftUnknown("weapon")
        registerMinecraftUnknown("enchantable")
        registerMinecraftUnknown("equippable")
        registerMinecraftUnknown("repairable")
        registerMinecraft<Unit, net.minecraft.util.Unit>("glider", {}, { net.minecraft.util.Unit.INSTANCE })
        registerMinecraft<Key, Identifier>("tooltip_style", Conversions.Adventure::adventure, Conversions.Adventure::vanilla)
        registerMinecraftUnknown("death_protection")
        registerMinecraftUnknown("blocks_attacks")
        registerMinecraftUnknown("stored_enchantments")
        registerMinecraft<Color, DyedColorComponent>("dyed_color", { Color(it.rgb) }, { DyedColorComponent(it.rgb) })
        registerMinecraft<Color, MapColorComponent>("map_color", { Color(it.rgb) }, { MapColorComponent(it.rgb) })
        registerMinecraft<Int, MapIdComponent>("map_id", { it.id }, { MapIdComponent(it) })
        registerMinecraftUnknown("map_decorations")
        registerMinecraftUnknown("map_post_processing")
        registerMinecraftUnknown("charged_projectiles")
        registerMinecraftUnknown("bundle_contents")
        registerMinecraftUnknown("potion_contents")
        registerMinecraftDirect<Float>("potion_duration_scale")
        registerMinecraftUnknown("suspicious_stew_effects")
        registerMinecraft<List<String>, WritableBookContentComponent>("writable_book_content", { it.pages.map { page -> page.raw } }, { WritableBookContentComponent(it.map { page -> RawFilteredPair(page, Optional.empty()) }) })
        registerMinecraftUnknown("written_book_content")
        registerMinecraftUnknown("trim")
        registerMinecraftUnknown("debug_stick_state")
        registerMinecraftUnknown("entity_data")
        registerMinecraftUnknown("bucket_entity_data")
        registerMinecraftUnknown("block_entity_data")
        registerMinecraftUnknown("instrument")
        registerMinecraftUnknown("provides_trim_material")
        registerMinecraft<Int, OminousBottleAmplifierComponent>("ominous_bottle_amplifier", { it.value }, { OminousBottleAmplifierComponent(it) })
        registerMinecraftUnknown("jukebox_playable")
        registerMinecraftUnknown("provides_banner_patterns")
        registerMinecraftUnknown("recipes")
        registerMinecraftUnknown("lodestone_tracker")
        registerMinecraftUnknown("firework_explosion")
        registerMinecraftUnknown("fireworks")
        registerMinecraftUnknown("profile")
        registerMinecraft<Key, Identifier>("note_block_sound", Conversions.Adventure::adventure, Conversions.Adventure::vanilla)
        registerMinecraftUnknown("banner_patterns")
        registerMinecraftUnknown("base_color")
        registerMinecraftUnknown("pot_decorations")
        registerMinecraftUnknown("container")
        // TODO: BlockStateItemComponent instead of raw map
        registerMinecraft<Map<String, String>, BlockStateComponent>("block_state", { Collections.unmodifiableMap(it.properties) }, { BlockStateComponent(it.toMap()) })
        registerMinecraftUnknown("bees")
        registerMinecraftUnknown("lock")
        registerMinecraftUnknown("container_loot")
        registerMinecraftUnknown("break_sound")
        registerMinecraftUnknown("villager/variant")
        registerMinecraftUnknown("wolf/variant")
        registerMinecraftUnknown("wolf/sound_variant")
        registerMinecraftUnknown("wolf/collar")
        registerMinecraftUnknown("fox/variant")
        registerMinecraftUnknown("salmon/size")
        registerMinecraftUnknown("parrot/variant")
        registerMinecraftUnknown("tropical_fish/pattern")
        registerMinecraftUnknown("tropical_fish/base_color")
        registerMinecraftUnknown("tropical_fish/pattern_color")
        registerMinecraftUnknown("mooshroom/variant")
        registerMinecraftUnknown("rabbit/variant")
        registerMinecraftUnknown("pig/variant")
        registerMinecraftUnknown("cow/variant")
        registerMinecraftUnknown("chicken/variant")
        registerMinecraftUnknown("frog/variant")
        registerMinecraftUnknown("horse/variant")
        registerMinecraftUnknown("painting/variant")
        registerMinecraftUnknown("llama/variant")
        registerMinecraftUnknown("axolotl/variant")
        registerMinecraftUnknown("cat/variant")
        registerMinecraftUnknown("cat/collar")
        registerMinecraftUnknown("sheep/color")
        registerMinecraftUnknown("shulker/color")
        return this
    }

    private fun <T> registerMinecraftDirect(id: String): ItemComponentTypeImpl<T, T> {
        return registerDirect(Key.key(Key.MINECRAFT_NAMESPACE, id))
    }

    fun <T> registerDirect(key: Key): ItemComponentTypeImpl<T, T> {
        return register(key, { it }, { it })
    }

    private fun registerMinecraftUnknown(id: String): ItemComponentTypeImpl<Unit, *> {
        return registerMinecraft(id, {}, { throw UnsupportedOperationException() })
    }

    private fun <T, V> registerMinecraft(id: String, snowballMapper: (V) -> T, vanillaMapper: (T) -> V): ItemComponentTypeImpl<T, V> {
        return register(Key.key(Key.MINECRAFT_NAMESPACE, id), snowballMapper, vanillaMapper)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, V> register(key: Key, snowballMapper: (V) -> T, vanillaMapper: (T) -> V): ItemComponentTypeImpl<T, V> {
        if (entries.containsKey(key))
            throw IllegalArgumentException("An item component type with key '$key' is already registered")
        val vanillaEntry = Registries.DATA_COMPONENT_TYPE.get(Conversions.Adventure.vanilla(key))
            ?: throw IllegalArgumentException("An item component type with key '$key' is not registered in the vanilla registry")
        val entry = ItemComponentTypeImpl(vanillaEntry as ComponentType<V>, snowballMapper, vanillaMapper)
        entries[key] = entry
        return entry
    }

    fun get(key: Key): ItemComponentType<*>? {
        return entries[key]
    }
}