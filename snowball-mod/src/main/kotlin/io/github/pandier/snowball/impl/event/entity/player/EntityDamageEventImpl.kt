package io.github.pandier.snowball.impl.event.entity.player

import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.entity.damage.DamageSource
import io.github.pandier.snowball.event.entity.EntityDamageEvent
import io.github.pandier.snowball.impl.event.ActionEventImpl
import io.github.pandier.snowball.impl.event.AllowEventImpl

interface EntityDamageEventImpl : EntityDamageEvent {
    class Allow(
        override val entity: LivingEntity,
        override val source: DamageSource,
        override val baseAmount: Float,
        override val amount: Float,
    ) : AllowEventImpl(), EntityDamageEventImpl, EntityDamageEvent.Allow

    class Action(
        override val entity: LivingEntity,
        override val source: DamageSource,
        override val baseAmount: Float,
        override var amount: Float,
    ) : ActionEventImpl(), EntityDamageEventImpl, EntityDamageEvent.Action

    class Canceled(
        override val entity: LivingEntity,
        override val source: DamageSource,
        override val baseAmount: Float,
        override val amount: Float,
    ) : EntityDamageEventImpl, EntityDamageEvent.Canceled

    class Post(
        override val entity: LivingEntity,
        override val source: DamageSource,
        override val baseAmount: Float,
        override val amount: Float,
    ) : EntityDamageEventImpl, EntityDamageEvent.Post
}