package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.LivingEntity

open class LivingEntityImpl(
    override val adaptee: net.minecraft.entity.LivingEntity
) : LivingEntity, EntityImpl(adaptee)
