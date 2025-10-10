package io.github.pandier.snowball.impl.event.entity.player

import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.event.entity.player.PlayerBlockBreakEvent
import io.github.pandier.snowball.impl.event.ActionEventImpl
import io.github.pandier.snowball.impl.event.AllowEventImpl
import io.github.pandier.snowball.math.Vector3i
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockState

sealed interface PlayerBlockBreakEventImpl : PlayerBlockBreakEvent {
    companion object ThreadLocals {
        val shouldDropItems = ThreadLocal<Boolean?>()
        val shouldDropExperience = ThreadLocal<Boolean?>()
    }

    class Allow(
        override val player: Player,
        override val world: World,
        override val position: Vector3i,
        override val blockState: BlockState,
    ) : AllowEventImpl(), PlayerBlockBreakEventImpl, PlayerBlockBreakEvent.Allow

    class Action(
        override val player: Player,
        override val world: World,
        override val position: Vector3i,
        override var blockState: BlockState,
    ) : ActionEventImpl(), PlayerBlockBreakEventImpl, PlayerBlockBreakEvent.Action {
        override var shouldDropExperience: Boolean = true
        override var shouldDropItems: Boolean = true
    }

    class Canceled(
        override val player: Player,
        override val world: World,
        override val position: Vector3i,
        override val blockState: BlockState,
    ) : PlayerBlockBreakEventImpl, PlayerBlockBreakEvent.Canceled

    class Post(
        override val player: Player,
        override val world: World,
        override val position: Vector3i,
        override val blockState: BlockState,
    ) : PlayerBlockBreakEventImpl, PlayerBlockBreakEvent.Post
}