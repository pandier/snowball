package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.scoreboard.TeamImpl;
import io.github.pandier.snowball.scoreboard.Team;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerTeam.class)
public class PlayerTeamMixin implements SnowballConvertible<Team> {
    @Unique private final SnowballAdapterHolder<TeamImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new TeamImpl((PlayerTeam) (Object) this));

    @Override
    public @NotNull Team snowball$get() {
        return impl$adapter.get();
    }
}
