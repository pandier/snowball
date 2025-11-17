package io.github.pandier.snowball.impl.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.scoreboard.ExtendedScoreAccess;
import io.github.pandier.snowball.impl.scoreboard.ScoreboardImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.world.scores.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Scoreboard.class)
public class ScoreboardMixin implements SnowballConvertible<io.github.pandier.snowball.scoreboard.Scoreboard> {
    @Unique private final SnowballAdapterHolder<ScoreboardImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new ScoreboardImpl((Scoreboard) (Object) this));

    @Override
    public io.github.pandier.snowball.scoreboard.@NotNull Scoreboard snowball$get() {
        return impl$adapter.get();
    }

    @Inject(method = "getOrCreatePlayerScore(Lnet/minecraft/world/scores/ScoreHolder;Lnet/minecraft/world/scores/Objective;Z)Lnet/minecraft/world/scores/ScoreAccess;",
            cancellable = true,
            at = @At("RETURN"))
    public void inject$extendedScoreAccess(ScoreHolder scoreHolder, Objective objective, boolean bl, CallbackInfoReturnable<ScoreAccess> cir, @Local Score score) {
        ScoreAccess access = cir.getReturnValue();
        cir.setReturnValue(new ExtendedScoreAccess() {
            @Override
            public int get() {
                return access.get();
            }

            @Override
            public void set(int i) {
                access.set(i);
            }

            @Override
            public boolean locked() {
                return access.locked();
            }

            @Override
            public void unlock() {
                access.unlock();
            }

            @Override
            public void lock() {
                access.lock();
            }

            @Override
            public @org.jspecify.annotations.Nullable Component display() {
                return access.display();
            }

            @Override
            public void display(@org.jspecify.annotations.Nullable Component component) {
                access.display(component);
            }

            @Override
            public void numberFormatOverride(@org.jspecify.annotations.Nullable NumberFormat numberFormat) {
                access.numberFormatOverride(numberFormat);
            }

            @Override
            public @Nullable NumberFormat numberFormatOverride() {
                return score.numberFormat();
            }
        });
    }
}
