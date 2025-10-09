package io.github.pandier.snowball.inventory

import io.github.pandier.snowball.entity.EquipmentSlot
import io.github.pandier.snowball.item.ItemStack

public interface PlayerInventory : Inventory {

    /**
     * The main part of the inventory containing both the [hotbar] and [storage].
     */
    public val main: Inventory

    /**
     * The 3x9 storage part of the inventory without the hotbar.
     */
    public val storage: Inventory

    /**
     * The hotbar.
     */
    public val hotbar: Hotbar

    /**
     * Just the armor part of the inventory.
     */
    public val armor: Inventory

    /**
     * The remaining part of the inventory containing both the [armor] and the offhand.
     */
    public val equipment: Inventory

    public operator fun get(slot: EquipmentSlot): ItemStack

    public operator fun set(slot: EquipmentSlot, stack: ItemStack): ItemStack

    public fun getEquipmentIndex(slot: EquipmentSlot): Int
}
