package io.github.pandier.snowball.impl.scoreboard

import io.github.pandier.snowball.impl.Conversions
import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.scoreboard.Team
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.ChatFormatting
import net.minecraft.world.scores.PlayerTeam
import org.jetbrains.annotations.UnmodifiableView
import java.util.Collections

class TeamImpl(
    override val adaptee: PlayerTeam
) : SnowballAdapter(), Team {

    override val name: String
        get() = adaptee.name

    override var displayName: Component
        get() = adaptee.displayName.let(Conversions.Adventure::adventure)
        set(value) { adaptee.displayName = value.let(Conversions.Adventure::vanilla) }

    override var color: NamedTextColor?
        get() = adaptee.color.let(Conversions.Adventure::adventureOrNull)
        set(value) { adaptee.color = value?.let(Conversions.Adventure::vanilla) ?: ChatFormatting.RESET }

    override var prefix: Component
        get() = adaptee.playerPrefix.let(Conversions.Adventure::adventure)
        set(value) { adaptee.setPlayerPrefix(value.let(Conversions.Adventure::vanilla)) }

    override var suffix: Component
        get() = adaptee.playerSuffix.let(Conversions.Adventure::adventure)
        set(value) { adaptee.setPlayerSuffix(value.let(Conversions.Adventure::vanilla)) }

    override var allowFriendlyFire: Boolean
        get() = adaptee.isAllowFriendlyFire
        set(value) { adaptee.isAllowFriendlyFire = value }

    override var canSeeFriendlyInvisible: Boolean
        get() = adaptee.canSeeFriendlyInvisibles()
        set(value) { adaptee.setSeeFriendlyInvisibles(value) }

    override var nameTagVisibility: Team.Visibility
        get() = adaptee.nameTagVisibility.let(Conversions::snowball)
        set(value) { adaptee.nameTagVisibility = value.let(Conversions::vanilla) }

    override var deathMessageVisibility: Team.Visibility
        get() = adaptee.deathMessageVisibility.let(Conversions::snowball)
        set(value) { adaptee.deathMessageVisibility = value.let(Conversions::vanilla) }

    override var collisionRule: Team.CollisionRule
        get() = adaptee.collisionRule.let(Conversions::snowball)
        set(value) { adaptee.collisionRule = value.let(Conversions::vanilla) }

    override val members: @UnmodifiableView Collection<String>
        get() = Collections.unmodifiableCollection(adaptee.players)

    override fun addMember(name: String) {
        if (isRemoved()) error("Cannot modify team '$name' because it has been removed")
        adaptee.scoreboard.addPlayerToTeam(name, adaptee)
    }

    override fun removeMember(name: String) {
        if (isRemoved()) error("Cannot modify team '$name' because it has been removed")
        adaptee.scoreboard.removePlayerFromTeam(name, adaptee)
    }

    override fun remove() {
        if (isRemoved()) return
        adaptee.scoreboard.removePlayerTeam(adaptee)
    }

    private fun isRemoved(): Boolean {
        return adaptee.scoreboard.getPlayerTeam(adaptee.name) == null
    }
}