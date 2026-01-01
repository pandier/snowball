package io.github.pandier.snowball.entity.player

import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import io.github.pandier.snowball.profile.GameProfile
import io.github.pandier.snowball.scoreboard.Scoreboard
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBarViewer
import net.kyori.adventure.text.`object`.PlayerHeadObjectContents
import org.jetbrains.annotations.UnmodifiableView

public interface Player : LivingEntity, Audience, BossBarViewer, PlayerHeadObjectContents.SkinSource {
    public val gameProfile: GameProfile

    /**
     * The [GameMode] of the player.
     */
    public var gameMode: GameMode

    /**
     * The player's current food level.
     */
    public var foodLevel: Int

    /**
     * The player's current saturation level.
     *
     * Saturation is consumed before food level.
     */
    public var saturation: Float

    /**
     * The player's current exhaustion level.
     *
     * Exhaustion is increased by performing actions that exhaust the player,
     * and when it reaches a certain amount, saturation or food level is decreased.
     */
    public var exhaustion: Float

    /**
     * The player's inventory.
     */
    public val inventory: PlayerInventory

    /**
     * The [Scoreboard] that the player is viewing. This can either be
     * [Server.scoreboard][io.github.pandier.snowball.server.Server.scoreboard] or a custom [Scoreboard] instance.
     */
    public var scoreboard: Scoreboard

    /**
     * Gives the specified [stack] to the player by inserting it into their inventory and playing a sound effect.
     * The supplied stack is modified to reflect the result of the operation.
     *
     * Returns the amount that was given.
     *
     * @see giveOrDrop
     */
    public fun give(stack: ItemStack): Int {
        return give(stack, false)
    }

    /**
     * Gives the specified [stack] to the player by inserting it into their inventory.
     * If [silent] is `true`, no sound effect will be played. The supplied stack **is modified**
     * to reflect the result of the operation.
     *
     * Returns the amount that was given.
     *
     * @see giveOrDrop
     */
    public fun give(stack: ItemStack, silent: Boolean): Int

    /**
     * Gives the specified [stack] to the player by inserting it into their inventory, playing a sound effect
     * and dropping the remaining items on the floor.
     *
     * The supplied stack is **not modified**.
     */
    public fun giveOrDrop(stack: ItemStackView) {
        giveOrDrop(stack, false)
    }

    /**
     * Gives the specified [stack] to the player by inserting it into their inventory
     * and dropping the remaining items on the floor. If [silent] is `true`, no sound effect will be played.
     *
     * The supplied stack is **not modified**.
     */
    public fun giveOrDrop(stack: ItemStackView, silent: Boolean)

    override fun activeBossBars(): @UnmodifiableView Collection<BossBar?>
}