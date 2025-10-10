package io.github.pandier.snowball.entity.damage

import io.github.pandier.snowball.Snowball
import io.github.pandier.snowball.registry.RegistryReference
import net.kyori.adventure.key.Key

public object DamageTypes {
    @JvmField public val ARROW: RegistryReference<DamageType> = ref("arrow")
    @JvmField public val BAD_RESPAWN_POINT: RegistryReference<DamageType> = ref("bad_respawn_point")
    @JvmField public val CACTUS: RegistryReference<DamageType> = ref("cactus")
    @JvmField public val CAMPFIRE: RegistryReference<DamageType> = ref("campfire")
    @JvmField public val CRAMMING: RegistryReference<DamageType> = ref("cramming")
    @JvmField public val DRAGON_BREATH: RegistryReference<DamageType> = ref("dragon_breath")
    @JvmField public val DROWN: RegistryReference<DamageType> = ref("drown")
    @JvmField public val DRY_OUT: RegistryReference<DamageType> = ref("dry_out")
    @JvmField public val ENDER_PEARL: RegistryReference<DamageType> = ref("ender_pearl")
    @JvmField public val EXPLOSION: RegistryReference<DamageType> = ref("explosion")
    @JvmField public val FALL: RegistryReference<DamageType> = ref("fall")
    @JvmField public val FALLING_ANVIL: RegistryReference<DamageType> = ref("falling_anvil")
    @JvmField public val FALLING_BLOCK: RegistryReference<DamageType> = ref("falling_block")
    @JvmField public val FALLING_STALACTITE: RegistryReference<DamageType> = ref("falling_stalactite")
    @JvmField public val FIREBALL: RegistryReference<DamageType> = ref("fireball")
    @JvmField public val FIREWORKS: RegistryReference<DamageType> = ref("fireworks")
    @JvmField public val FLY_INTO_WALL: RegistryReference<DamageType> = ref("fly_into_wall")
    @JvmField public val FREEZE: RegistryReference<DamageType> = ref("freeze")
    @JvmField public val GENERIC: RegistryReference<DamageType> = ref("generic")
    @JvmField public val GENERIC_KILL: RegistryReference<DamageType> = ref("generic_kill")
    @JvmField public val HOT_FLOOR: RegistryReference<DamageType> = ref("hot_floor")
    @JvmField public val IN_FIRE: RegistryReference<DamageType> = ref("in_fire")
    @JvmField public val IN_WALL: RegistryReference<DamageType> = ref("in_wall")
    @JvmField public val INDIRECT_MAGIC: RegistryReference<DamageType> = ref("indirect_magic")
    @JvmField public val LAVA: RegistryReference<DamageType> = ref("lava")
    @JvmField public val LIGHTNING_BOLT: RegistryReference<DamageType> = ref("lightning_bolt")
    @JvmField public val MACE_SMASH: RegistryReference<DamageType> = ref("mace_smash")
    @JvmField public val MAGIC: RegistryReference<DamageType> = ref("magic")
    @JvmField public val MOB_ATTACK: RegistryReference<DamageType> = ref("mob_attack")
    @JvmField public val MOB_ATTACK_NO_AGGRO: RegistryReference<DamageType> = ref("mob_attack_no_aggro")
    @JvmField public val MOB_PROJECTILE: RegistryReference<DamageType> = ref("mob_projectile")
    @JvmField public val ON_FIRE: RegistryReference<DamageType> = ref("on_fire")
    @JvmField public val OUT_OF_WORLD: RegistryReference<DamageType> = ref("out_of_world")
    @JvmField public val OUTSIDE_BORDER: RegistryReference<DamageType> = ref("outside_border")
    @JvmField public val PLAYER_ATTACK: RegistryReference<DamageType> = ref("player_attack")
    @JvmField public val PLAYER_EXPLOSION: RegistryReference<DamageType> = ref("player_explosion")
    @JvmField public val SONIC_BOOM: RegistryReference<DamageType> = ref("sonic_boom")
    @JvmField public val SPIT: RegistryReference<DamageType> = ref("spit")
    @JvmField public val STALAGMITE: RegistryReference<DamageType> = ref("stalagmite")
    @JvmField public val STARVE: RegistryReference<DamageType> = ref("starve")
    @JvmField public val STING: RegistryReference<DamageType> = ref("sting")
    @JvmField public val SWEET_BERRY_BUSH: RegistryReference<DamageType> = ref("sweet_berry_bush")
    @JvmField public val THORNS: RegistryReference<DamageType> = ref("thorns")
    @JvmField public val THROWN: RegistryReference<DamageType> = ref("thrown")
    @JvmField public val TRIDENT: RegistryReference<DamageType> = ref("trident")
    @JvmField public val UNATTRIBUTED_FIREBALL: RegistryReference<DamageType> = ref("unattributed_fireball")
    @JvmField public val WIND_CHARGE: RegistryReference<DamageType> = ref("wind_charge")
    @JvmField public val WITHER: RegistryReference<DamageType> = ref("wither")
    @JvmField public val WITHER_SKULL: RegistryReference<DamageType> = ref("wither_skull")

    public fun ref(entry: DamageType): RegistryReference<DamageType> {
        return Snowball.registries.damageType(entry)
    }

    private fun ref(id: String): RegistryReference<DamageType> {
        return Snowball.registries.damageType(Key.key(Key.MINECRAFT_NAMESPACE, id))
    }
}
