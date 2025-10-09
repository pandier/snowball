package io.github.pandier.snowball.entity.player

import io.github.pandier.snowball.entity.LivingEntity
import io.github.pandier.snowball.inventory.PlayerInventory
import io.github.pandier.snowball.profile.GameProfile
import net.kyori.adventure.audience.Audience

public interface Player : LivingEntity, Audience {
    public val gameProfile: GameProfile

    public var gameMode: GameMode

    public val inventory: PlayerInventory
}