package io.github.pandier.snowball.impl.scoreboard

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.scoreboard.Criterion
import io.github.pandier.snowball.scoreboard.Objective
import net.minecraft.world.scores.criteria.ObjectiveCriteria

class CriterionImpl(
    override val adaptee: ObjectiveCriteria,
) : SnowballAdapter(), Criterion {
    override val defaultRenderType: Objective.RenderType
        get() = adaptee.defaultRenderType.let(Conversions::snowball)
}
