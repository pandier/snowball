package io.github.pandier.snowball.impl.entity

import com.google.common.collect.Collections2
import io.github.pandier.snowball.entity.Attribute
import io.github.pandier.snowball.entity.AttributeModifier
import io.github.pandier.snowball.entity.AttributeType
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import net.kyori.adventure.key.Key
import net.minecraft.world.entity.ai.attributes.AttributeInstance

class AttributeImpl(
    override val adaptee: AttributeInstance,
) : SnowballAdapter(), Attribute {
    override val type: AttributeType
        get() = adaptee.attribute.value().let(Conversions::snowball)
    override val modifiers: Collection<AttributeModifier>
        get() = Collections2.transform(adaptee.modifiers, Conversions::snowball)
    override var baseValue: Double
        get() = adaptee.baseValue
        set(value) { adaptee.baseValue = value }
    override val value: Double
        get() = adaptee.value

    override fun addModifier(modifier: AttributeModifier): Boolean {
        if (hasModifier(modifier.key)) return false
        adaptee.addPermanentModifier(modifier.let(Conversions::vanilla))
        return true
    }

    override fun removeModifier(key: Key): Boolean =
        adaptee.removeModifier(key.let(Conversions.Adventure::vanilla))

    override fun clearModifiers() =
        adaptee.removeModifiers()

    override fun getModifier(key: Key): AttributeModifier? =
        adaptee.getModifier(key.let(Conversions.Adventure::vanilla))?.let(Conversions::snowball)

    override fun hasModifier(key: Key): Boolean =
        adaptee.hasModifier(key.let(Conversions.Adventure::vanilla))
}