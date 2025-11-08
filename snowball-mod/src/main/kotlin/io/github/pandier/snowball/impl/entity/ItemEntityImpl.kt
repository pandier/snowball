package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.ItemEntity
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.impl.mixin.ItemEntityAccessor
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.math.Vector3d
import net.minecraft.world.entity.EntityReference
import java.util.UUID

private const val SHORT_MIN: Int = Short.MIN_VALUE.toInt()
private const val SHORT_MAX: Int = Short.MAX_VALUE.toInt()

open class ItemEntityImpl(
    adaptee: net.minecraft.world.entity.item.ItemEntity
) : EntityImpl(adaptee), ItemEntity {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: net.minecraft.world.entity.item.ItemEntity = adaptee

    override var itemStack: ItemStack
        get() = adaptee.item.let(Conversions::snowball)
        set(value) { adaptee.item = (value as ItemStackImpl).adaptee }

    override var thrower: Entity?
        get() = adaptee.owner?.let(Conversions::snowball)
        set(value) = (adaptee as ItemEntityAccessor).`snowball$setThrower`(EntityReference.of(value?.let { (it as EntityImpl).adaptee }))

    override var target: UUID?
        get() = (adaptee as ItemEntityAccessor).`snowball$getTarget`()
        set(value) = adaptee.setTarget(value)

    override var age: Int
        get() = adaptee.age
        set(value) { (adaptee as ItemEntityAccessor).`snowball$setAge`(minOf(maxOf(value, SHORT_MIN), SHORT_MAX)) }

    override var pickupDelay: Int
        get() = (adaptee as ItemEntityAccessor).`snowball$getPickupDelay`()
        set(value) { adaptee.setPickUpDelay(minOf(maxOf(value, SHORT_MIN), SHORT_MAX)) }

    override val isInfiniteLifetime: Boolean
        get() = adaptee.age == SHORT_MIN

    override val isInfinitePickupDelay: Boolean
        get() = pickupDelay == SHORT_MAX

    override fun setInfiniteLifetime() {
        adaptee.setUnlimitedLifetime()
    }

    override fun setInfinitePickupDelay() {
        adaptee.setNeverPickUp()
    }

    override fun setNaturalDropProperties() {
        val random = adaptee.level().random
        velocity = Vector3d(random.nextDouble() * 0.2 - 0.1, 0.2, random.nextDouble() * 0.2 - 0.1)
        adaptee.setDefaultPickUpDelay()
    }
}