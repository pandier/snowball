package io.github.pandier.snowball.entity.projectile

import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.item.component.FireworksComponent

public interface FireworkRocket : Projectile {

    /**
     * The firework effects of the rocket.
     */
    public var fireworks: FireworksComponent

    /**
     * The [LivingEntity] this firework is attached to.
     *
     * When attached to an entity, the firework will remain position on the entity.
     * If the entity is gliding, it will receive a boost in the direction they are looking.
     */
    public var attachedTo: LivingEntity?

    /**
     * If the firework was shot at an angle (i.e. from a crossbow).
     *
     * The firework will fly upwards if false.
     */
    public var isShotAtAngle: Boolean

    /**
     * The number of ticks the firework will detonate on. Also known as the lifetime of the rocket.
     */
    public var detonationTicks: Int

    /**
     * How many ticks this firework has lived for.
     */
    public var life: Int

    /**
     * Detonates this firework immediately.
     */
    public fun detonate()
}
