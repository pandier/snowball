package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.adapter.SnowballAdapterHolder;
import io.github.pandier.snowball.impl.bridge.SnowballConvertible;
import io.github.pandier.snowball.impl.event.entity.player.PlayerBlockBreakEventImpl;
import io.github.pandier.snowball.impl.world.block.BlockTypeImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(Block.class)
public class BlockMixin implements SnowballConvertible<io.github.pandier.snowball.world.block.BlockType> {
    @Unique private final SnowballAdapterHolder<BlockTypeImpl> impl$adapter = new SnowballAdapterHolder.Lazy<>(() -> new BlockTypeImpl((Block) (Object) this));

    @Override
    public @NotNull io.github.pandier.snowball.world.block.BlockType snowball$get() {
        return impl$adapter.get();
    }

    @Inject(method = "popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void inject$popResourceBreakEvent(Level level, Supplier<ItemEntity> supplier, ItemStack itemStack, CallbackInfo ci) {
        if (Boolean.FALSE.equals(PlayerBlockBreakEventImpl.ThreadLocals.getShouldDropItems().get())) {
            ci.cancel();
        }
    }

    @Inject(method = "popExperience", at = @At("HEAD"), cancellable = true)
    private static void inject$popExperienceBreakEvent(ServerLevel serverLevel, BlockPos blockPos, int i, CallbackInfo ci) {
        if (Boolean.FALSE.equals(PlayerBlockBreakEventImpl.ThreadLocals.getShouldDropExperience().get())) {
            ci.cancel();
        }
    }
}
