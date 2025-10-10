package io.github.pandier.snowball.impl.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.event.entity.player.PlayerBlockBreakEventImpl;
import io.github.pandier.snowball.impl.world.block.BlockTypeImpl;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public class BlockMixin implements SnowballConvertible<io.github.pandier.snowball.world.block.BlockType> {
    @Unique private final SnowballAdapterHolder<BlockTypeImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new BlockTypeImpl((Block) (Object) this));

    @Override
    public @NotNull io.github.pandier.snowball.world.block.BlockType snowball$get() {
        return impl$adapter.get();
    }

    @ModifyExpressionValue(method = "popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private static boolean inject$popResourceBreakEvent(boolean original) {
        // We're checking if it's not false, because it can be null (and it should be allowed if it is)
        return original && !Boolean.FALSE.equals(PlayerBlockBreakEventImpl.ThreadLocals.getShouldDropItems().get());
    }

    @ModifyExpressionValue(method = "popExperience",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private static boolean inject$popExperienceBreakEvent(boolean original) {
        // We're checking if it's not false, because it can be null (and it should be allowed if it is)
        return original && !Boolean.FALSE.equals(PlayerBlockBreakEventImpl.ThreadLocals.getShouldDropExperience().get());
    }
}
