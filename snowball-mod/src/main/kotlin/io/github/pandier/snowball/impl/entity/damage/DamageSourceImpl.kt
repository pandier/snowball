package io.github.pandier.snowball.impl.entity.damage

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.entity.damage.DamageType
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.entity.LivingEntityImpl
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.math.Vector3d
import net.kyori.adventure.text.Component

class DamageSourceImpl(
    override val adaptee: net.minecraft.world.damagesource.DamageSource
) : SnowballAdapter(adaptee), DamageSource {
    override val type: DamageType
        get() = adaptee.type().let(Conversions::snowball)
    override val causingEntity: Entity?
        get() = adaptee.entity?.let(Conversions::snowball)
    override val directEntity: Entity?
        get() = adaptee.directEntity?.let(Conversions::snowball)
    override val position: Vector3d?
        get() = adaptee.sourcePosition?.let { Vector3d(it.x, it.y, it.z) }
    override val weapon: ItemStack?
        get() = adaptee.weaponItem?.let(Conversions::snowball)

    override fun getDeathMessage(receiver: LivingEntity): Component =
        adaptee.getLocalizedDeathMessage((receiver as LivingEntityImpl).adaptee)
            .let(Conversions.Adventure::adventure)
}