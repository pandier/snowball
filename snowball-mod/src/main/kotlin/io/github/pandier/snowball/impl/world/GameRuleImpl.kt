package io.github.pandier.snowball.impl.world

import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.world.GameRule

class GameRuleImpl<T : Any>(
    override val adaptee: net.minecraft.world.level.gamerules.GameRule<T>,
) : SnowballAdapter(), GameRule<T> {
    override val valueClass: Class<T>
        get() = adaptee.valueClass()

    override val defaultValue: T
        get() = adaptee.defaultValue()
}
