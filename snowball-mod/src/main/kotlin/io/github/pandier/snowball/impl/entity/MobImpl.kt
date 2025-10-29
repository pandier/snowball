package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.Mob
import io.github.pandier.snowball.impl.mixin.MobAccessor

open class MobImpl(
    adaptee: net.minecraft.world.entity.Mob
) : LivingEntityImpl(adaptee), Mob {
    @Suppress("CanBePrimaryConstructorProperty")
    override val adaptee: net.minecraft.world.entity.Mob = adaptee

    override var isPersistent: Boolean
        get() = adaptee.isPersistenceRequired
        set(value) = (adaptee as MobAccessor).`snowball$setPersistanceRequired`(value)

    override var hasAI: Boolean
        get() = !adaptee.isNoAi
        set(value) { adaptee.isNoAi = !value }
}
