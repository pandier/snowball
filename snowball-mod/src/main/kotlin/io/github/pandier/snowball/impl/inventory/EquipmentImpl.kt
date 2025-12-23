package io.github.pandier.snowball.impl.inventory

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.entity.Equipment
import io.github.pandier.snowball.entity.EquipmentSlot
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import net.minecraft.world.entity.EntityEquipment

class EquipmentImpl(
    override val adaptee: EntityEquipment,
) : SnowballAdapter(), Equipment {
    override fun get(slot: EquipmentSlot): ItemStack =
        adaptee.get(slot.let(Conversions::vanilla)).let(Conversions::snowball)

    override fun set(slot: EquipmentSlot, stack: ItemStackView): ItemStack =
        adaptee.set(slot.let(Conversions::vanilla), (stack.copy() as ItemStackImpl).adaptee).let(Conversions::snowball)

    override fun isEmpty(): Boolean =
        adaptee.isEmpty()

    override fun clear() =
        adaptee.clear()
}
