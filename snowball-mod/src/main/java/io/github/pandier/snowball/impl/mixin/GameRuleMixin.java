package io.github.pandier.snowball.impl.mixin;

import com.google.common.base.Suppliers;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.world.GameRuleImpl;
import net.minecraft.world.level.gamerules.GameRule;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Supplier;

@Mixin(GameRule.class)
public class GameRuleMixin implements SnowballConvertible<io.github.pandier.snowball.world.GameRule<?>> {
    @Unique private final Supplier<GameRuleImpl<?>> impl$adapter = Suppliers.memoize(() -> new GameRuleImpl<>((net.minecraft.world.level.gamerules.GameRule<?>) (Object) this));

    @Override
    public io.github.pandier.snowball.world.@NotNull GameRule<?> snowball$get() {
        return impl$adapter.get();
    }
}
