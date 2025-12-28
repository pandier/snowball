package io.github.pandier.snowball.impl.entity.projectile

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.projectile.Projectile
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.entity.EntityImpl

open class ProjectileImpl(
    adaptee: net.minecraft.world.entity.projectile.Projectile
) : EntityImpl(adaptee), Projectile {
    override val adaptee: net.minecraft.world.entity.projectile.Projectile
        get() = super.adapteeInternal as net.minecraft.world.entity.projectile.Projectile

    override var shooter: Entity?
        get() = adaptee.owner?.let(Conversions::snowball)
        set(value) { adaptee.owner = value?.let { (it as EntityImpl).adaptee } }
}
