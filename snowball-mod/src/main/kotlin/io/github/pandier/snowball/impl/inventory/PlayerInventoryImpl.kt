package io.github.pandier.snowball.impl.inventory

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.impl.mixin.PlayerInventoryAccessor
import io.github.pandier.snowball.inventory.EquipmentSlot
import io.github.pandier.snowball.inventory.Hotbar
import io.github.pandier.snowball.inventory.Inventory
import io.github.pandier.snowball.inventory.PlayerInventory
import io.github.pandier.snowball.item.ItemStack
import net.minecraft.entity.player.PlayerInventory.HOTBAR_SIZE
import net.minecraft.entity.player.PlayerInventory.MAIN_SIZE

private val equipmentToIndex = net.minecraft.entity.player.PlayerInventory.EQUIPMENT_SLOTS
    .int2ObjectEntrySet()
    .associate { it.value.let(Conversions::snowball) to it.intKey }

class PlayerInventoryImpl(
    override val adaptee: net.minecraft.entity.player.PlayerInventory
) : InventoryImpl(adaptee), PlayerInventory {
    override val main: Inventory = ProxyInventory(this, 0, MAIN_SIZE)
    override val storage: Inventory = ProxyInventory(this, HOTBAR_SIZE, MAIN_SIZE - HOTBAR_SIZE)
    override val hotbar: Hotbar = object : Hotbar, ProxyInventory(this, 0, HOTBAR_SIZE) {
        override var selectedIndex: Int
            get() = adaptee.selectedSlot
            set(value) { adaptee.selectedSlot = value }
    }

    override val armor: Inventory = ProxyInventory(this, MAIN_SIZE, 4)
    override val equipment: Inventory = ProxyInventory(this, MAIN_SIZE, size - MAIN_SIZE)

    override fun get(slot: EquipmentSlot): ItemStack =
        (adaptee as PlayerInventoryAccessor).`snowball$getEquipment`().get(slot.let(Conversions::vanilla)).let(Conversions::snowball)

    override fun set(slot: EquipmentSlot, stack: ItemStack): ItemStack =
        (adaptee as PlayerInventoryAccessor).`snowball$getEquipment`().put(slot.let(Conversions::vanilla), (stack as ItemStackImpl).adaptee).let(Conversions::snowball)

    override fun getEquipmentIndex(slot: EquipmentSlot): Int {
        return equipmentToIndex[slot] ?: run {
            if (slot == EquipmentSlot.MAIN_HAND) return hotbar.selectedIndex
            throw IllegalArgumentException("Unsupported equipment slot: $slot")
        }
    }
}
