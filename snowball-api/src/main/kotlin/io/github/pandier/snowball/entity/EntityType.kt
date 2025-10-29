package io.github.pandier.snowball.entity

import net.kyori.adventure.text.Component

public interface EntityType<T : Entity> {
    public val name: Component
}
