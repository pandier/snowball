package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.inventory.EquipmentImpl
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.impl.mixin.LivingEntityAccessor
import io.github.pandier.snowball.entity.Equipment
import io.github.pandier.snowball.entity.EquipmentSlot
import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.impl.entity.damage.DamageSourceImpl
import io.github.pandier.snowball.item.ItemStack
import net.minecraft.server.level.ServerLevel

open class LivingEntityImpl(
    adaptee: net.minecraft.world.entity.LivingEntity
) : EntityImpl(adaptee), LivingEntity {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: net.minecraft.world.entity.LivingEntity = adaptee

    override var health: Float
        get() = adaptee.health
        set(value) {
            if (adaptee.health <= 0f) {
                adaptee.kill(adaptee.level() as ServerLevel)
            }
            adaptee.health = value
        }

    override val maxHealth: Float
        get() = adaptee.maxHealth

    override val equipment: Equipment = EquipmentImpl((adaptee as LivingEntityAccessor).`snowball$getEquipment`())

    override fun equip(slot: EquipmentSlot, item: ItemStack) {
        adaptee.setItemSlot(slot.let(Conversions::vanilla), (item as ItemStackImpl).adaptee)
    }

    override fun damage(amount: Float, source: DamageSource) {
        val level = adaptee.level() as? ServerLevel ?: return
        adaptee.hurtServer(level, (source as DamageSourceImpl).adaptee, amount)
    }

    override fun kill() {
        val level = adaptee.level() as? ServerLevel ?: return
        adaptee.kill(level)
    }
}
