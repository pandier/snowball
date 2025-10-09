package io.github.pandier.snowball.event.player

import io.github.pandier.snowball.entity.player.Player
import io.github.pandier.snowball.event.ActionEvent
import io.github.pandier.snowball.event.AllowEvent
import io.github.pandier.snowball.event.Event
import io.github.pandier.snowball.math.Vector3i
import io.github.pandier.snowball.world.World
import io.github.pandier.snowball.world.block.BlockState

/**
 * A multi-stage event for breaking a block by a player.
 */
public interface PlayerBlockBreakEvent : Event {

    public val player: Player

    public val world: World

    /**
     * Position of the block that will be broken.
     */
    public val position: Vector3i

    /**
     * The current block state.
     */
    public val blockState: BlockState

    /**
     * Determines if breaking the block should be allowed.
     *
     * @see AllowEvent
     */
    public interface Allow : PlayerBlockBreakEvent, AllowEvent

    /**
     * Called before breaking the block. Default can be prevented.
     *
     * @see ActionEvent
     */
    public interface Action : PlayerBlockBreakEvent, ActionEvent {

        /**
         * If the block should be able to drop its loot.
         */
        public var shouldDropItems: Boolean

        /**
         * If the block should be able to drop experience.
         */
        public var shouldDropExperience: Boolean
    }

    /**
     * Called if breaking the block was denied.
     */
    public interface Canceled : PlayerBlockBreakEvent

    /**
     * Called after breaking the block.
     */
    public interface Post : PlayerBlockBreakEvent
}