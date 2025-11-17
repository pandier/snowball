package io.github.pandier.snowball.entity

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.registry.RegistryReference
import net.kyori.adventure.key.Key

public object AttributeTypes {
    @JvmField public val ARMOR: RegistryReference<AttributeType> = ref("armor")
    @JvmField public val ARMOR_TOUGHNESS: RegistryReference<AttributeType> = ref("armor_toughness")
    @JvmField public val ATTACK_DAMAGE: RegistryReference<AttributeType> = ref("attack_damage")
    @JvmField public val ATTACK_KNOCKBACK: RegistryReference<AttributeType> = ref("attack_knockback")
    @JvmField public val ATTACK_SPEED: RegistryReference<AttributeType> = ref("attack_speed")
    @JvmField public val BLOCK_BREAK_SPEED: RegistryReference<AttributeType> = ref("block_break_speed")
    @JvmField public val BLOCK_INTERACTION_RANGE: RegistryReference<AttributeType> = ref("block_interaction_range")
    @JvmField public val BURNING_TIME: RegistryReference<AttributeType> = ref("burning_time")
    @JvmField public val CAMERA_DISTANCE: RegistryReference<AttributeType> = ref("camera_distance")
    @JvmField public val EXPLOSION_KNOCKBACK_RESISTANCE: RegistryReference<AttributeType> = ref("explosion_knockback_resistance")
    @JvmField public val ENTITY_INTERACTION_RANGE: RegistryReference<AttributeType> = ref("entity_interaction_range")
    @JvmField public val FALL_DAMAGE_MULTIPLIER: RegistryReference<AttributeType> = ref("fall_damage_multiplier")
    @JvmField public val FLYING_SPEED: RegistryReference<AttributeType> = ref("flying_speed")
    @JvmField public val FOLLOW_RANGE: RegistryReference<AttributeType> = ref("follow_range")
    @JvmField public val GRAVITY: RegistryReference<AttributeType> = ref("gravity")
    @JvmField public val JUMP_STRENGTH: RegistryReference<AttributeType> = ref("jump_strength")
    @JvmField public val KNOCKBACK_RESISTANCE: RegistryReference<AttributeType> = ref("knockback_resistance")
    @JvmField public val LUCK: RegistryReference<AttributeType> = ref("luck")
    @JvmField public val MAX_ABSORPTION: RegistryReference<AttributeType> = ref("max_absorption")
    @JvmField public val MAX_HEALTH: RegistryReference<AttributeType> = ref("max_health")
    @JvmField public val MINING_EFFICIENCY: RegistryReference<AttributeType> = ref("mining_efficiency")
    @JvmField public val MOVEMENT_EFFICIENCY: RegistryReference<AttributeType> = ref("movement_efficiency")
    @JvmField public val MOVEMENT_SPEED: RegistryReference<AttributeType> = ref("movement_speed")
    @JvmField public val OXYGEN_BONUS: RegistryReference<AttributeType> = ref("oxygen_bonus")
    @JvmField public val SAFE_FALL_DISTANCE: RegistryReference<AttributeType> = ref("safe_fall_distance")
    @JvmField public val SCALE: RegistryReference<AttributeType> = ref("scale")
    @JvmField public val SNEAKING_SPEED: RegistryReference<AttributeType> = ref("sneaking_speed")
    @JvmField public val SPAWN_REINFORCEMENTS: RegistryReference<AttributeType> = ref("spawn_reinforcements")
    @JvmField public val STEP_HEIGHT: RegistryReference<AttributeType> = ref("step_height")
    @JvmField public val SUBMERGED_MINING_SPEED: RegistryReference<AttributeType> = ref("submerged_mining_speed")
    @JvmField public val SWEEPING_DAMAGE_RATIO: RegistryReference<AttributeType> = ref("sweeping_damage_ratio")
    @JvmField public val TEMPT_RANGE: RegistryReference<AttributeType> = ref("tempt_range")
    @JvmField public val WATER_MOVEMENT_EFFICIENCY: RegistryReference<AttributeType> = ref("water_movement_efficiency")
    @JvmField public val WAYPOINT_TRANSMIT_RANGE: RegistryReference<AttributeType> = ref("waypoint_transmit_range")
    @JvmField public val WAYPOINT_RECEIVE_RANGE: RegistryReference<AttributeType> = ref("waypoint_receive_range")

    private fun ref(id: String): RegistryReference<AttributeType> = Snowball.registries.attributeType(Key.key(Key.MINECRAFT_NAMESPACE, id))
}
