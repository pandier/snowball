package io.github.pandier.snowball.entity

import io.github.pandier.snowball.inventory.Equipment
import io.github.pandier.snowball.inventory.EquipmentSlot
import io.github.pandier.snowball.item.ItemStack

public interface LivingEntity : Entity {
    public val equipment: Equipment

    /**
     * Equips the given [item] in the given [slot].
     * This differs from [Equipment.set] in that it plays sounds and triggers events.
     */
    public fun equip(slot: EquipmentSlot, item: ItemStack)
}
