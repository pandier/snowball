package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.inventory.EquipmentImpl
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.impl.mixin.LivingEntityAccessor
import io.github.pandier.snowball.inventory.Equipment
import io.github.pandier.snowball.inventory.EquipmentSlot
import io.github.pandier.snowball.item.ItemStack

open class LivingEntityImpl(
    adaptee: net.minecraft.world.entity.LivingEntity
) : EntityImpl(adaptee), LivingEntity {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: net.minecraft.world.entity.LivingEntity = adaptee

    override val equipment: Equipment = EquipmentImpl((adaptee as LivingEntityAccessor).`snowball$getEquipment`())

    override fun equip(slot: EquipmentSlot, item: ItemStack) {
        adaptee.setItemSlot(slot.let(Conversions::vanilla), (item as ItemStackImpl).adaptee)
    }
}
