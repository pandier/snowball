package io.github.pandier.snowball.event.entity

import io.github.pandier.snowball.entity.EntityTypes
import io.github.pandier.snowball.entity.ItemEntity
import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.event.Event
import io.github.pandier.snowball.item.ItemStack
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

/**
 * Called right after an entity dies.
 */
public interface EntityDeathEvent : Event {

    /**
     * The entity that died.
     */
    public val entity: LivingEntity

    /**
     * The damage source that killed the entity.
     */
    public val source: DamageSource

    /**
     * An audience that will receive the death message.
     *
     * This is usually [Audience.empty].
     */
    public var audience: Audience

    /**
     * The original unmodified death message.
     *
     * A death message is created even for entities that usually don't have a death message (like zombies).
     */
    public val originalMessage: Component?

    /**
     * The death message that will be sent to the [audience].
     */
    public var message: Component?

    /**
     * The item drops as entities that will spawn in once the event is done.
     * Can be modified, but new entries can't already exist in the world.
     *
     * Consider using [addDrop] for adding new drops.
     */
    public val drops: MutableList<ItemEntity>

    /**
     * The amount of experience that will be dropped.
     */
    public var experience: Int

    /**
     * Adds a new drop to this event and returns the added item entity.
     */
    public fun addDrop(stack: ItemStack): ItemEntity {
        return entity.world.createEntity(EntityTypes.ITEM, entity.position).apply {
            itemStack = stack
            setNaturalDropProperties()
            drops.add(this)
        }
    }
}
