package io.github.pandier.snowball.impl.event.entity.player

import io.github.pandier.snowball.entity.player.Hand
import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.event.entity.player.PlayerBlockPlaceEvent
import io.github.pandier.snowball.impl.event.ActionEventImpl
import io.github.pandier.snowball.impl.event.AllowEventImpl
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import io.github.pandier.snowball.math.Vector3i
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockState

sealed interface PlayerBlockPlaceEventImpl : PlayerBlockPlaceEvent {

    class Allow(
        override val player: Player,
        override val world: World,
        override val position: Vector3i,
        override val blockState: BlockState,
        override val itemStack: ItemStackView,
        override val hand: Hand,
    ) : AllowEventImpl(), PlayerBlockPlaceEventImpl, PlayerBlockPlaceEvent.Allow

    class Action(
        override val player: Player,
        override val world: World,
        override val position: Vector3i,
        override var blockState: BlockState,
        override val defaultBlockState: BlockState,
        override val itemStack: ItemStack,
        override val hand: Hand,
    ) : ActionEventImpl(), PlayerBlockPlaceEventImpl, PlayerBlockPlaceEvent.Action

    class Canceled(
        override val player: Player,
        override val world: World,
        override val position: Vector3i,
        override val blockState: BlockState,
        override val itemStack: ItemStackView,
        override val hand: Hand,
    ) : PlayerBlockPlaceEventImpl, PlayerBlockPlaceEvent.Canceled

    class Post(
        override val player: Player,
        override val world: World,
        override val position: Vector3i,
        override val blockState: BlockState,
        override val previousBlockState: BlockState,
        override val itemStack: ItemStackView,
        override val hand: Hand,
    ) : PlayerBlockPlaceEventImpl, PlayerBlockPlaceEvent.Post
}