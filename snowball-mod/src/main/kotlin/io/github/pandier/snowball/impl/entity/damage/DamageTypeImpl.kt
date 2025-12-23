package io.github.pandier.snowball.impl.entity.damage

import io.github.pandier.snowball.entity.damage.DamageType
import io.github.pandier.snowball.impl.adapter.SnowballAdapter

class DamageTypeImpl(
    override val adaptee: net.minecraft.world.damagesource.DamageType
) : SnowballAdapter(), DamageType {
    override val translationId: String
        get() = adaptee.msgId
    override val exhaustion: Float
        get() = adaptee.exhaustion
}