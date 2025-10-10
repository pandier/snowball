package io.github.pandier.snowball.event.entity.player

import io.github.pandier.snowball.event.Event
import io.github.pandier.snowball.math.Location
import io.github.pandier.snowball.profile.GameProfile
import io.github.pandier.snowball.world.World

/**
 * Called before [PlayerJoinEvent] before the player is placed into the world.
 * Use this to set the player's initial [location] or [world].
 */
public class PlayerPrepareEvent(
    public val profile: GameProfile,
    public val originalWorld: World,
    public val originalLocation: Location,
) : Event {
    public var world: World = originalWorld
    public var location: Location = originalLocation
}
