package io.github.pandier.snowball.impl.world

import com.google.common.collect.Collections2
import com.google.common.collect.Iterables
import io.github.pandier.snowball.entity.Entity
import io.github.pandier.snowball.entity.EntityType
import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.impl.entity.EntityImpl
import io.github.pandier.snowball.impl.entity.EntityTypeImpl
import io.github.pandier.snowball.impl.world.block.BlockStateImpl
import io.github.pandier.snowball.math.Vector3d
import io.github.pandier.snowball.world.GameRule
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockState
import net.kyori.adventure.key.Key
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.jetbrains.annotations.UnmodifiableView
import java.util.UUID

class WorldImpl(
    override val adaptee: ServerLevel
) : SnowballAdapter(), World {
    override val key: Key
        get() = Conversions.Adventure.adventure(adaptee.dimension().identifier())

    override val entities: Iterable<Entity>
        get() = Iterables.transform(adaptee.allEntities, Conversions::snowball)

    override fun <T : Entity> createEntity(type: EntityType<T>, position: Vector3d): T {
        return (type as EntityTypeImpl<T, *>).create(adaptee, position)
    }

    override fun spawnEntity(entity: Entity): Boolean {
        return adaptee.addFreshEntity((entity as EntityImpl).adaptee)
    }

    override fun getEntity(uuid: UUID): Entity? {
        return adaptee.getEntity(uuid)?.let(Conversions::snowball)
    }

    override fun getEntity(id: Int): Entity? {
        return adaptee.getEntity(id)?.let(Conversions::snowball)
    }

    override val players: @UnmodifiableView Collection<Player>
        get() = Collections2.transform(adaptee.players(), Conversions::snowball)

    override fun setBlockState(x: Int, y: Int, z: Int, state: BlockState) {
        adaptee.setBlockAndUpdate(BlockPos(x, y, z), (state as BlockStateImpl).adaptee)
    }

    override fun getBlockState(x: Int, y: Int, z: Int): BlockState {
        return Conversions.snowball(adaptee.getBlockState(BlockPos(x, y, z)))
    }

    override fun <T : Any> setGameRule(rule: GameRule<T>, value: T) {
        adaptee.gameRules.set((rule as GameRuleImpl<T>).adaptee, value, adaptee.server)
    }

    override fun <T : Any> getGameRule(rule: GameRule<T>): T {
        return adaptee.gameRules.get((rule as GameRuleImpl<T>).adaptee)
    }

    override val seed: Long
        get() = adaptee.seed

    override var dayTime: Long
        get() = adaptee.dayTime
        set(value) { adaptee.dayTime = value }

    override val dayCount: Long
        get() = adaptee.dayCount
}