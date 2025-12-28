package io.github.pandier.snowball.impl.registry

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.item.component.ItemComponentTypeImpl
import io.github.pandier.snowball.item.ItemRarity
import io.github.pandier.snowball.math.Color
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.server.network.Filterable
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.BlockItemStateProperties
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.component.MapItemColor
import net.minecraft.world.item.component.OminousBottleAmplifier
import net.minecraft.world.item.component.WritableBookContent
import net.minecraft.world.level.saveddata.maps.MapId
import java.util.Collections
import java.util.Optional

/**
 * A registry that properly handles [io.github.pandier.snowball.item.component.ItemComponentType]s.
 */
class SnowballItemComponentTypeRegistry {
    private val entries = mutableMapOf<Key, ItemComponentTypeImpl<*, *>>()
    private val vanillaToSnowball = mutableMapOf<DataComponentType<*>, ItemComponentTypeImpl<*, *>>()

    fun registerDefaults(): SnowballItemComponentTypeRegistry {
        // TODO: Implement all unknowns
        registerMinecraftUnknown("custom_data")
        registerMinecraftDirect<Int>("max_stack_size")
        registerMinecraftDirect<Int>("max_damage")
        registerMinecraftDirect<Int>("damage")
        registerMinecraft<Unit, net.minecraft.util.Unit>("unbreakable", {}, { net.minecraft.util.Unit.INSTANCE })
        registerMinecraft<Component, net.minecraft.network.chat.Component>("custom_name", Conversions.Adventure::adventure, Conversions.Adventure::vanilla)
        registerMinecraft<Component, net.minecraft.network.chat.Component>("item_name", Conversions.Adventure::adventure, Conversions.Adventure::vanilla)
        registerMinecraft<Key, Identifier>("item_model", Conversions.Adventure::adventure, Conversions.Adventure::vanilla)
        registerMinecraft<List<Component>, ItemLore>("lore", { it.lines.map(Conversions.Adventure::adventure) }, { ItemLore(it.map(Conversions.Adventure::vanilla)) })
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
        registerMinecraft<Color, DyedItemColor>("dyed_color", { Color(it.rgb) }, { DyedItemColor(it.rgb) })
        registerMinecraft<Color, MapItemColor>("map_color", { Color(it.rgb) }, { MapItemColor(it.rgb) })
        registerMinecraft<Int, MapId>("map_id", { it.id }, { MapId(it) })
        registerMinecraftUnknown("map_decorations")
        registerMinecraftUnknown("map_post_processing")
        registerMinecraftUnknown("charged_projectiles")
        registerMinecraftUnknown("bundle_contents")
        registerMinecraftUnknown("potion_contents")
        registerMinecraftDirect<Float>("potion_duration_scale")
        registerMinecraftUnknown("suspicious_stew_effects")
        registerMinecraft<List<String>, WritableBookContent>("writable_book_content", { it.pages().map { page -> page.raw } }, { WritableBookContent(it.map { page -> Filterable(page, Optional.empty()) }) })
        registerMinecraftUnknown("written_book_content")
        registerMinecraftUnknown("trim")
        registerMinecraftUnknown("debug_stick_state")
        registerMinecraftUnknown("entity_data")
        registerMinecraftUnknown("bucket_entity_data")
        registerMinecraftUnknown("block_entity_data")
        registerMinecraftUnknown("instrument")
        registerMinecraftUnknown("provides_trim_material")
        registerMinecraft<Int, OminousBottleAmplifier>("ominous_bottle_amplifier", { it.value }, { OminousBottleAmplifier(it) })
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
        registerMinecraft<Map<String, String>, BlockItemStateProperties>("block_state", { Collections.unmodifiableMap(it.properties) }, { BlockItemStateProperties(it.toMap()) })
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

    private inline fun <reified T : Any> registerMinecraftDirect(id: String): ItemComponentTypeImpl<T, T> {
        return registerDirect(Key.key(Key.MINECRAFT_NAMESPACE, id))
    }

    inline fun <reified T : Any> registerDirect(key: Key): ItemComponentTypeImpl<T, T> {
        return registerDirect(key, T::class.java)
    }

    fun <T : Any> registerDirect(key: Key, type: Class<T>): ItemComponentTypeImpl<T, T> {
        return register(key, type, { it }, { it })
    }

    private fun registerMinecraftUnknown(id: String): ItemComponentTypeImpl<Unit, *> {
        return registerMinecraft(id, null, {}, { throw UnsupportedOperationException() })
    }

    private inline fun <reified T, V : Any> registerMinecraft(id: String, noinline snowballMapper: (V) -> T, noinline vanillaMapper: (T) -> V): ItemComponentTypeImpl<T, V> {
        return registerMinecraft(id, T::class.java, snowballMapper, vanillaMapper)
    }

    private fun <T, V : Any> registerMinecraft(id: String, type: Class<T>?, snowballMapper: (V) -> T, vanillaMapper: (T) -> V): ItemComponentTypeImpl<T, V> {
        return register(Key.key(Key.MINECRAFT_NAMESPACE, id), type, snowballMapper, vanillaMapper)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T, V : Any> register(key: Key, type: Class<T>?, snowballMapper: (V) -> T, vanillaMapper: (T) -> V): ItemComponentTypeImpl<T, V> {
        if (entries.containsKey(key))
            throw IllegalArgumentException("An item component type with key '$key' is already registered")
        val vanilla = BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(Conversions.Adventure.vanilla(key))
            ?: throw IllegalArgumentException("An item component type with key '$key' is not registered in the vanilla registry")
        val entry = ItemComponentTypeImpl(vanilla as DataComponentType<V>, type, snowballMapper, vanillaMapper)
        entries[key] = entry
        vanillaToSnowball[vanilla] = entry
        return entry
    }

    fun get(key: Key): ItemComponentTypeImpl<*, *>? {
        return entries[key]
            ?: BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(Conversions.Adventure.vanilla(key))
                ?.let(::fallback)
    }

    fun get(vanilla: DataComponentType<*>): ItemComponentTypeImpl<*, *> {
        return vanillaToSnowball[vanilla] ?: fallback(vanilla)
    }

    private fun fallback(vanilla: DataComponentType<*>): ItemComponentTypeImpl<*, *> {
        return ItemComponentTypeImpl(vanilla, null, {}, { throw UnsupportedOperationException() })
    }
}