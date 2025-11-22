package io.github.pandier.snowball.entity.player

import io.github.pandier.snowball.entity.EquipmentSlot
import io.github.pandier.snowball.inventory.Hotbar
import io.github.pandier.snowball.inventory.Inventory
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView

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

    public operator fun set(slot: EquipmentSlot, stack: ItemStackView): ItemStack

    public fun getEquipmentIndex(slot: EquipmentSlot): Int
}