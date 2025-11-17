package io.github.pandier.snowball.impl.scoreboard

import net.minecraft.network.chat.numbers.NumberFormat
import net.minecraft.world.scores.ScoreAccess

interface ExtendedScoreAccess : ScoreAccess {
    fun numberFormatOverride(): NumberFormat?
}
