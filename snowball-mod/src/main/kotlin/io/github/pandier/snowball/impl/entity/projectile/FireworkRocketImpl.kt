package io.github.pandier.snowball.impl.entity.projectile

import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.entity.projectile.FireworkRocket
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.bridge.FireworkRocketEntityBridge
import io.github.pandier.snowball.impl.entity.LivingEntityImpl
import io.github.pandier.snowball.item.component.FireworksComponent
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.projectile.FireworkRocketEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

open class FireworkRocketImpl(
    adaptee: FireworkRocketEntity
) : ProjectileImpl(adaptee), FireworkRocket {
    override val adaptee: FireworkRocketEntity
        get() = super.adapteeInternal as FireworkRocketEntity

    override var fireworks: FireworksComponent
        get() = adaptee.item.get(DataComponents.FIREWORKS)?.let(Conversions::snowball) ?: FireworksComponent.DEFAULT
        set(value) {
            (adaptee as FireworkRocketEntityBridge).`snowball$setItem`(ItemStack(Items.FIREWORK_ROCKET).apply {
                set(DataComponents.FIREWORKS, value.let(Conversions::vanilla))
            })
            detonationTicks = 10 * (1 + value.duration) + adaptee.random.nextInt(6) + adaptee.random.nextInt(7)
        }

    override var attachedTo: LivingEntity?
        get() = (adaptee as FireworkRocketEntityBridge).`snowball$getAttachedTo`()?.let(Conversions::snowball)
        set(value) { (adaptee as FireworkRocketEntityBridge).`snowball$setAttachedTo`(value?.let { (it as LivingEntityImpl).adaptee }) }

    override var isShotAtAngle: Boolean
        get() = adaptee.isShotAtAngle
        set(value) { (adaptee as FireworkRocketEntityBridge).`snowball$setShotAtAngle`(value) }

    override var detonationTicks: Int
        get() = (adaptee as FireworkRocketEntityBridge).`snowball$getLifetime`()
        set(value) { (adaptee as FireworkRocketEntityBridge).`snowball$setLifetime`(value) }

    override var life: Int
        get() = (adaptee as FireworkRocketEntityBridge).`snowball$getLife`()
        set(value) { (adaptee as FireworkRocketEntityBridge).`snowball$setLife`(value) }

    override fun detonate() {
        (adaptee as FireworkRocketEntityBridge).`snowball$detonate`()
    }
}
