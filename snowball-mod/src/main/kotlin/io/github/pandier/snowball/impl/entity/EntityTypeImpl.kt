package io.github.pandier.snowball.impl.entity

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.EntityType
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.bridge.SnowballConvertible
import io.github.pandier.snowball.math.Vector3d
import net.kyori.adventure.text.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason

class EntityTypeImpl<T : Entity, V : net.minecraft.world.entity.Entity>(
    override val adaptee: net.minecraft.world.entity.EntityType<V>,
) : SnowballAdapter(), EntityType<T> {

    override val name: Component
        get() = adaptee.description.let(Conversions.Adventure::adventure)

    @Suppress("UNCHECKED_CAST")
    fun create(level: ServerLevel, position: Vector3d): T {
        if (adaptee == net.minecraft.world.entity.EntityType.PLAYER)
            throw IllegalArgumentException("A player entity cannot be created")

        val entity = adaptee.create(level, EntitySpawnReason.EVENT)
            ?: error("EntityType#create returned null")
        entity.snapTo(position.x, position.y, position.z)

        // TODO: Make this type safe
        return (entity as SnowballConvertible<*>).`snowball$get`() as T
    }
}