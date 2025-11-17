package io.github.pandier.snowball.scoreboard

import io.github.pandier.snowball.entity.Entity
import net.kyori.adventure.text.Component

/**
 * A [Scoreboard] objective that tracks scores for entities.
 */
public interface Objective {

    /**
     * The name of the objective.
     */
    public val name: String

    /**
     * The [Criterion] of the objective.
     */
    public val criterion: Criterion

    /**
     * The objective's display name that appears on display slots, such as the sidebar.
     */
    public var displayName: Component

    /**
     * The [RenderType] of the objective.
     */
    public var renderType: RenderType

    /**
     * Whether to update a score's display when it's changed.
     */
    public var displayAutoUpdate: Boolean

    /**
     * The default [NumberFormat] used for scores that don't specify their own number format.
     */
    public var numberFormat: NumberFormat?

    /**
     * List of all score names inside this objective.
     */
    public val entries: List<String>

    /**
     * Gets a [Score] for the given [name]. Returns null if not found.
     */
    public fun getScore(name: String): Score?

    public fun getScore(entity: Entity): Score? {
        return getScore(entity.scoreboardName)
    }

    /**
     * Checks if the given [name] has a [Score].
     */
    public fun hasScore(name: String): Boolean

    public fun hasScore(entity: Entity): Boolean {
        return hasScore(entity.scoreboardName)
    }

    /**
     * Creates a new [Score] for the given [name].
     *
     * If a score with the given name already exists, it is returned.
     *
     * @throws IllegalStateException if the objective has been removed
     */
    public fun createScore(name: String): Score

    public fun createScore(entity: Entity): Score {
        return createScore(entity.scoreboardName)
    }

    /**
     * Removes the [Score] for the given [name].
     *
     * @throws IllegalStateException if the objective has been removed
     */
    public fun removeScore(name: String)

    public fun removeScore(entity: Entity) {
        removeScore(entity.scoreboardName)
    }

    /**
     * Removes this objective from the scoreboard.
     */
    public fun remove()

    public enum class RenderType {
        INTEGER,
        HEARTS,
    }
}
