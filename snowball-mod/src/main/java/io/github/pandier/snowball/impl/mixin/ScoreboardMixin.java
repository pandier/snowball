package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.scoreboard.ScoreboardImpl;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Scoreboard.class)
public class ScoreboardMixin implements SnowballConvertible<io.github.pandier.snowball.scoreboard.Scoreboard> {
    @Unique private final SnowballAdapterHolder<ScoreboardImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new ScoreboardImpl((Scoreboard) (Object) this));

    @Override
    public io.github.pandier.snowball.scoreboard.@NotNull Scoreboard snowball$get() {
        return impl$adapter.get();
    }
}
