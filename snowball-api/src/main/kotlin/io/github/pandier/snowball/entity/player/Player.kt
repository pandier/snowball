package io.github.pandier.snowball.entity.player

import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.inventory.PlayerInventory
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import io.github.pandier.snowball.profile.GameProfile
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBarViewer
import net.kyori.adventure.text.`object`.PlayerHeadObjectContents

public interface Player : LivingEntity, Audience, BossBarViewer, PlayerHeadObjectContents.SkinSource {
    public val gameProfile: GameProfile

    /**
     * The [GameMode] of the player.
     */
    public var gameMode: GameMode

    /**
     * The player's inventory.
     */
    public val inventory: PlayerInventory

    /**
     * Gives the specified [stack] to the player by inserting it into their inventory and playing a sound effect.
     * The supplied stack is modified to reflect the result of the operation.
     *
     * To drop remaining items on the floor, use [giveOrDrop].
     */
    public fun give(stack: ItemStack): Int {
        return give(stack, false)
    }

    /**
     * Gives the specified [stack] to the player by inserting it into their inventory.
     * If [silent] is `true`, no sound effect will be played. The supplied stack is modified
     * to reflect the result of the operation.
     *
     * To drop remaining items on the floor, use [giveOrDrop].
     */
    public fun give(stack: ItemStack, silent: Boolean): Int

    /**
     * Gives the specified [stack] to the player by inserting it into their inventory, playing a sound effect
     * and dropping the remaining items on the floor
     * The supplied stack is **NOT** modified.
     */
    public fun giveOrDrop(stack: ItemStackView) {
        giveOrDrop(stack, false)
    }

    /**
     * Gives the specified [stack] to the player by inserting it into their inventory
     * and dropping the remaining items on the floor. If [silent] is `true`, no sound effect will be played.
     * The supplied stack is **NOT** modified.
     */
    public fun giveOrDrop(stack: ItemStackView, silent: Boolean)
}