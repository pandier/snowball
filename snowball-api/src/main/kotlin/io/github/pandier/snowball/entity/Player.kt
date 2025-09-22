package io.github.pandier.snowball.entity

import io.github.pandier.snowball.inventory.PlayerInventory
import io.github.pandier.snowball.profile.GameProfile
import net.kyori.adventure.audience.Audience

public interface Player : LivingEntity, Audience {
    public val gameProfile: GameProfile

    public val inventory: PlayerInventory
}
