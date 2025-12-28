package io.github.pandier.snowball.entity.projectile

import io.github.pandier.snowball.entity.Entity

public interface Projectile : Entity {
    public val shooter: Entity?
}
