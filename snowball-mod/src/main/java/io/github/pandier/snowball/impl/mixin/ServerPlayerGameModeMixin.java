package io.github.pandier.snowball.impl.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.pandier.snowball.Snowball;
import io.github.pandier.snowball.entity.player.Player;
import io.github.pandier.snowball.event.entity.player.PlayerBlockBreakEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.event.entity.player.PlayerBlockBreakEventImpl;
import io.github.pandier.snowball.math.Vector3i;
import io.github.pandier.snowball.world.World;
import io.github.pandier.snowball.world.block.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow @Final protected ServerPlayer player;
    @Shadow protected ServerLevel level;

    @Inject(method = "destroyBlock",
            order = 1100, // Execute after Fabric API's block break event
            cancellable = true,
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/block/state/BlockState;"))
    public void inject$breakEventAction(
            BlockPos blockPos, CallbackInfoReturnable<Boolean> cir,
            @Local net.minecraft.world.level.block.state.BlockState vBlockState, @Share("blockState") LocalRef<BlockState> blockStateShare
    ) {
        Player player = Conversions.INSTANCE.snowball(this.player);
        World world = Conversions.INSTANCE.snowball(this.level);
        Vector3i position = new Vector3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        BlockState blockState = Conversions.INSTANCE.snowball(vBlockState);

        PlayerBlockBreakEvent.Allow allow = new PlayerBlockBreakEventImpl.Allow(player, world, position, blockState);
        Snowball.getEventManager().dispatch(allow);

        if (!allow.getAllowed()) {
            PlayerBlockBreakEvent.Canceled canceled = new PlayerBlockBreakEventImpl.Canceled(player, world, position, blockState);
            Snowball.getEventManager().dispatch(canceled);

            cir.setReturnValue(false);
            return;
        }

        PlayerBlockBreakEvent.Action action = new PlayerBlockBreakEventImpl.Action(player, world, position, blockState);
        Snowball.getEventManager().dispatch(action);

        if (action.isDefaultPrevented()) {
            cir.setReturnValue(false);
            return;
        }

        blockStateShare.set(blockState);

        PlayerBlockBreakEventImpl.ThreadLocals.getShouldDropItems().set(action.getShouldDropItems());
        PlayerBlockBreakEventImpl.ThreadLocals.getShouldDropExperience().set(action.getShouldDropExperience());
    }

    @Inject(method = "destroyBlock",
            at = {@At(value = "RETURN", ordinal = 4), @At(value = "RETURN", ordinal = 3)})
    public void inject$breakEventPost(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Share("blockState") LocalRef<BlockState> blockState) {
        PlayerBlockBreakEventImpl.ThreadLocals.getShouldDropItems().remove();
        PlayerBlockBreakEventImpl.ThreadLocals.getShouldDropExperience().remove();

        PlayerBlockBreakEvent.Post event = new PlayerBlockBreakEventImpl.Post(
                Conversions.INSTANCE.snowball(player),
                Conversions.INSTANCE.snowball(level),
                new Vector3i(pos.getX(), pos.getY(), pos.getZ()),
                blockState.get()
        );

        Snowball.getEventManager().dispatch(event);
    }
}
