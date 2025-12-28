package io.github.pandier.snowball.world

import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.EntityType
import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.math.Vector3i
import io.github.pandier.snowball.world.block.BlockState
import net.kyori.adventure.key.Key
import org.jetbrains.annotations.UnmodifiableView
import java.util.UUID
import java.util.function.Consumer
import java.util.function.Supplier

public interface World {
    public val key: Key

    public val entities: @UnmodifiableView Iterable<Entity>

    public fun <T : Entity> createEntity(type: EntityType<T>, position: Vector3d): T

    public fun <T : Entity> createEntity(type: Supplier<EntityType<T>>, position: Vector3d): T {
        return createEntity(type.get(), position)
    }

    public fun spawnEntity(entity: Entity): Boolean

    public fun <T : Entity> spawnEntity(type: EntityType<T>, position: Vector3d): T {
        return createEntity(type, position).also {
            spawnEntity(it)
        }
    }

    public fun <T : Entity> spawnEntity(type: EntityType<T>, position: Vector3d, block: Consumer<T>): T {
        return createEntity(type, position).also {
            block.accept(it)
            spawnEntity(it)
        }
    }

    public fun <T : Entity> spawnEntity(type: Supplier<EntityType<T>>, position: Vector3d, block: Consumer<T>): T {
        return spawnEntity(type.get(), position, block)
    }

    public fun getEntity(uuid: UUID): Entity?

    public fun getEntity(id: Int): Entity?

    public val players: @UnmodifiableView Collection<Player>

    public fun setBlockState(pos: Vector3i, state: BlockState) {
        setBlockState(pos.x, pos.y, pos.z, state)
    }

    public fun setBlockState(x: Int, y: Int, z: Int, state: BlockState)

    public fun getBlockState(pos: Vector3i): BlockState {
        return getBlockState(pos.x, pos.y, pos.z)
    }

    public fun getBlockState(x: Int, y: Int, z: Int): BlockState

    public fun <T : Any> setGameRule(rule: GameRule<T>, value: T)

    public fun <T : Any> setGameRule(rule: Supplier<GameRule<T>>, value: T) {
        return setGameRule(rule.get(), value)
    }

    public fun <T : Any> getGameRule(rule: GameRule<T>): T

    public fun <T : Any> getGameRule(rule: Supplier<GameRule<T>>): T {
        return getGameRule(rule.get())
    }

    public val seed: Long

    public var dayTime: Long

    public val dayCount: Long
}
