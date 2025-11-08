package io.github.pandier.snowball.impl.event.entity

import io.github.pandier.snowball.entity.ItemEntity
import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.event.entity.EntityDeathEvent
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

class EntityDeathEventImpl(
    override val entity: LivingEntity,
    override val source: DamageSource,
    override var audience: Audience,
    override val originalMessage: Component?,
    override val drops: MutableList<ItemEntity>,
    override var experience: Int,
) : EntityDeathEvent {
    override var message: Component? = originalMessage
}
