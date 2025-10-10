package io.github.pandier.snowball.entity

import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.item.ItemStack

public interface LivingEntity : Entity {

    /**
     * The current health of the entity.
     *
     * Ranges from 0 to [maxHealth]. The entity is killed when set to zero.
     */
    public var health: Float

    /**
     * The maximum health of the entity.
     */
    public val maxHealth: Float

    public val equipment: Equipment

    /**
     * Equips the given [item] in the given [slot].
     * This differs from [Equipment.set] in that it plays sounds and triggers events.
     */
    public fun equip(slot: EquipmentSlot, item: ItemStack)

    /**
     * Damages the entity with the given [amount] and [source].
     */
    public fun damage(amount: Float, source: DamageSource)

    /**
     * Kills the entity.
     */
    public fun kill()
}
