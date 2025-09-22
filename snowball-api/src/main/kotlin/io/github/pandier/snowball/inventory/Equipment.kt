package io.github.pandier.snowball.inventory

import io.github.pandier.snowball.item.ItemStack

/**
 * Represents entity equipment, where each [EquipmentSlot] is assigned an [ItemStack].
 */
public interface Equipment {

    public operator fun get(slot: EquipmentSlot): ItemStack

    public operator fun set(slot: EquipmentSlot, stack: ItemStack): ItemStack

    public fun isEmpty(): Boolean

    public fun clear()
}
