package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.AttributeType
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import net.minecraft.world.entity.ai.attributes.Attribute

class AttributeTypeImpl(
    override val adaptee: Attribute
) : SnowballAdapter(adaptee), AttributeType {
    override val defaultValue: Double
        get() = adaptee.defaultValue
}
