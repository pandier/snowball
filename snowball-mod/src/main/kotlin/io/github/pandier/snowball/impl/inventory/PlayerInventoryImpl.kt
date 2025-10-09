package io.github.pandier.snowball.impl.inventory

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.impl.mixin.InventoryAccessor
import io.github.pandier.snowball.entity.EquipmentSlot
import io.github.pandier.snowball.inventory.Hotbar
import io.github.pandier.snowball.inventory.Inventory
import io.github.pandier.snowball.inventory.PlayerInventory
import io.github.pandier.snowball.item.ItemStack
import net.minecraft.world.entity.player.Inventory.SELECTION_SIZE
import net.minecraft.world.entity.player.Inventory.INVENTORY_SIZE

private val equipmentToIndex = net.minecraft.world.entity.player.Inventory.EQUIPMENT_SLOT_MAPPING
    .int2ObjectEntrySet()
    .associate { it.value.let(Conversions::snowball) to it.intKey }

class PlayerInventoryImpl(
    override val adaptee: net.minecraft.world.entity.player.Inventory 
) : InventoryImpl(adaptee), PlayerInventory {
    override val main: Inventory = ProxyInventory(this, 0, INVENTORY_SIZE)
    override val storage: Inventory = ProxyInventory(this, SELECTION_SIZE, INVENTORY_SIZE - SELECTION_SIZE)
    override val hotbar: Hotbar = object : Hotbar, ProxyInventory(this, 0, SELECTION_SIZE) {
        override var selectedIndex: Int
            get() = adaptee.selectedSlot
            set(value) { adaptee.selectedSlot = value }
    }

    override val armor: Inventory = ProxyInventory(this, INVENTORY_SIZE, 4)
    override val equipment: Inventory = ProxyInventory(this, INVENTORY_SIZE, size - INVENTORY_SIZE)

    override fun get(slot: EquipmentSlot): ItemStack =
        (adaptee as InventoryAccessor).`snowball$getEquipment`().get(slot.let(Conversions::vanilla)).let(Conversions::snowball)

    override fun set(slot: EquipmentSlot, stack: ItemStack): ItemStack =
        (adaptee as InventoryAccessor).`snowball$getEquipment`().set(slot.let(Conversions::vanilla), (stack as ItemStackImpl).adaptee).let(Conversions::snowball)

    override fun insert(stack: ItemStack): Int {
        val originalCount = stack.count
        adaptee.add((stack as ItemStackImpl).adaptee)
        return originalCount - stack.count
    }

    override fun getEquipmentIndex(slot: EquipmentSlot): Int {
        return equipmentToIndex[slot] ?: run {
            if (slot == EquipmentSlot.MAIN_HAND) return hotbar.selectedIndex
            throw IllegalArgumentException("Unsupported equipment slot: $slot")
        }
    }
}
