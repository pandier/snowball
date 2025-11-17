package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.scoreboard.ObjectiveImpl;
import net.minecraft.world.scores.Objective;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Objective.class)
public class ObjectiveMixin implements SnowballConvertible<io.github.pandier.snowball.scoreboard.Objective> {
    @Unique private final SnowballAdapterHolder<ObjectiveImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new ObjectiveImpl((Objective) (Object) this));

    @Override
    public io.github.pandier.snowball.scoreboard.@NotNull Objective snowball$get() {
        return impl$adapter.get();
    }
}
