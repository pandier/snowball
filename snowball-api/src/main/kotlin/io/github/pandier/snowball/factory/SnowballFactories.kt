package io.github.pandier.snowball.factory

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.entity.damage.DamageType
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemType
import io.github.pandier.snowball.math.Vector3d

public interface SnowballFactories {
    public fun emptyItemStack(): ItemStack
    public fun itemStack(type: ItemType, count: Int): ItemStack
    public fun damageSource(type: DamageType, directEntity: Entity?, causingEntity: Entity?, position: Vector3d?): DamageSource
}
