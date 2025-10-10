package io.github.pandier.snowball.entity.damage

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.math.Vector3d
import net.kyori.adventure.text.Component
import java.util.function.Supplier

/**
 * Contains details about the source of damage.
 */
public interface DamageSource {
    public companion object {
        @JvmStatic
        public fun of(type: Supplier<DamageType>): DamageSource =
            of(type.get())

        @JvmStatic
        public fun of(type: DamageType): DamageSource =
            of(type, null, null, null)

        @JvmStatic
        public fun of(type: Supplier<DamageType>, directEntity: Entity?): DamageSource =
            of(type.get(), directEntity)

        @JvmStatic
        public fun of(type: DamageType, directEntity: Entity?): DamageSource =
            of(type, directEntity, null, null)

        @JvmStatic
        public fun of(type: Supplier<DamageType>, directEntity: Entity?, causingEntity: Entity?): DamageSource =
            of(type.get(), directEntity, causingEntity)

        @JvmStatic
        public fun of(type: DamageType, directEntity: Entity?, causingEntity: Entity?): DamageSource =
            Snowball.factories.damageSource(type, directEntity, causingEntity, null)

        @JvmStatic
        public fun of(type: Supplier<DamageType>, position: Vector3d?): DamageSource =
            of(type.get(), position)

        @JvmStatic
        public fun of(type: DamageType, position: Vector3d?): DamageSource =
            Snowball.factories.damageSource(type, null, null, position)

        @JvmStatic
        public fun of(type: Supplier<DamageType>, directEntity: Entity?, causingEntity: Entity?, position: Vector3d?): DamageSource =
            of(type.get(), directEntity, causingEntity, position)

        @JvmStatic
        public fun of(type: DamageType, directEntity: Entity?, causingEntity: Entity?, position: Vector3d?): DamageSource =
            Snowball.factories.damageSource(type, directEntity, causingEntity, position)
    }

    /**
     * The type of the inflicted damage.
     */
    public val type: DamageType

    /**
     * The indirect [Entity] that caused the damage to occur.
     *
     * This is the entity to which the damage is attributed if the receiver is killed.
     * That can be a skeleton that shot an arrow, for example.
     */
    public val causingEntity: Entity?

    /**
     * The [Entity] that directly inflicted the damage.
     *
     * That can be an arrow that hit the receiver, for example.
     */
    public val directEntity: Entity?

    /**
     * The position of the damage source.
     *
     * Used for knockback, for example.
     */
    public val position: Vector3d?

    /**
     * The weapon used to inflict the damage.
     *
     * That can be a bow used to shoot an arrow or a sword in hand, for example.
     */
    public val weapon: ItemStack?

    /**
     * Returns the death message for the given [receiver].
     */
    public fun getDeathMessage(receiver: LivingEntity): Component
}