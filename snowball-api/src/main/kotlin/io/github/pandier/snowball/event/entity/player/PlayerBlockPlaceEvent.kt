package io.github.pandier.snowball.event.entity.player

import io.github.pandier.snowball.entity.player.Hand
import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.event.ActionEvent
import io.github.pandier.snowball.event.AllowEvent
import io.github.pandier.snowball.event.Event
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import io.github.pandier.snowball.math.Vector3i
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockState

/**
 * A multi-stage event for placing a block by a player.
 */
public interface PlayerBlockPlaceEvent : Event {

    public val player: Player

    public val world: World

    /**
     * Position where the new block will be placed.
     */
    public val position: Vector3i

    /**
     * The new block state that will be placed.
     */
    public val blockState: BlockState

    /**
     * The item stack that was or will be used to place the block.
     */
    public val itemStack: ItemStackView

    /**
     * The hand that was used to place the block.
     */
    public val hand: Hand

    /**
     * Determines if placing the block should be allowed.
     *
     * @see AllowEvent
     */
    public interface Allow : PlayerBlockPlaceEvent, AllowEvent

    /**
     * Called before placing the block. Default can be prevented.
     *
     * @see ActionEvent
     */
    public interface Action : PlayerBlockPlaceEvent, ActionEvent {

        /**
         * The new block state that will be placed.
         *
         * This can be modified, but if the action is prevented, then the world won't be affected.
         */
        public override var blockState: BlockState

        /**
         * The block state that would be placed by default.
         */
        public val defaultBlockState: BlockState

        /**
         * The item stack that was used to place the block. Can be modified.
         */
        override val itemStack: ItemStack
    }

    /**
     * Called if the block placement was denied by [Allow].
     */
    public interface Canceled : PlayerBlockPlaceEvent

    /**
     * Called after placing the block.
     */
    public interface Post : PlayerBlockPlaceEvent {

        /**
         * The block state before the block was placed.
         */
        public val previousBlockState: BlockState
    }
}