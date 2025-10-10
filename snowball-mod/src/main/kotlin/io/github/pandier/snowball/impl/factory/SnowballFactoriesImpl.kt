package io.github.pandier.snowball.impl.factory

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.entity.damage.DamageType
import io.github.pandier.snowball.factory.SnowballFactories
import io.github.pandier.snowball.impl.SnowballImpl
import io.github.pandier.snowball.impl.entity.EntityImpl
import io.github.pandier.snowball.impl.entity.damage.DamageSourceImpl
import io.github.pandier.snowball.impl.entity.damage.DamageTypeImpl
import io.github.pandier.snowball.impl.item.EmptyItemStack
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.impl.item.ItemTypeImpl
import io.github.pandier.snowball.impl.mixin.DamageSourceAccessor
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.math.Vector3d
import net.minecraft.core.registries.Registries
import net.minecraft.world.phys.Vec3

class SnowballFactoriesImpl : SnowballFactories {
    override fun emptyItemStack(): ItemStack {
        return EmptyItemStack
    }

    override fun itemStack(type: ItemType, count: Int): ItemStack {
        return ItemStackImpl(net.minecraft.world.item.ItemStack((type as ItemTypeImpl).adaptee, count))
    }

    override fun damageSource(type: DamageType, directEntity: Entity?, causingEntity: Entity?, position: Vector3d?): DamageSource {
        val typeHolder = SnowballImpl.server.adaptee.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE)
            .wrapAsHolder((type as DamageTypeImpl).adaptee)
        return DamageSourceImpl(DamageSourceAccessor.`snowball$init`(
            typeHolder,
            directEntity?.let { (it as EntityImpl).adaptee },
            causingEntity?.let { (it as EntityImpl).adaptee },
            position?.let { Vec3(it.x, it.y, it.z) },
        ))
    }
}