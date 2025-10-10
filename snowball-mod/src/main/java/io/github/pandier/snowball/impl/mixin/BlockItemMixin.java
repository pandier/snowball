package io.github.pandier.snowball.impl.mixin;

import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.pandier.snowball.Snowball;
import io.github.pandier.snowball.entity.player.Hand;
import io.github.pandier.snowball.entity.player.Player;
import io.github.pandier.snowball.event.entity.player.PlayerBlockPlaceEvent;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.event.entity.player.PlayerBlockPlaceEventImpl;
import io.github.pandier.snowball.impl.world.block.BlockStateImpl;
import io.github.pandier.snowball.item.ItemStack;
import io.github.pandier.snowball.math.Vector3i;
import io.github.pandier.snowball.world.World;
import io.github.pandier.snowball.world.block.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @ModifyVariable(method = "place", at = @At(value = "STORE"), ordinal = 0)
    public net.minecraft.world.level.block.state.BlockState inject$placeEvent(
            net.minecraft.world.level.block.state.BlockState vBlockState,
            BlockPlaceContext ctx, @Cancellable CallbackInfoReturnable<InteractionResult> cir,
            @Share("previousBlockState") LocalRef<net.minecraft.world.level.block.state.BlockState> vPreviousBlockState,
            @Share("preItemStack") LocalRef<ItemStack> preItemStack
    ) {
        if (vBlockState == null || !(ctx.getPlayer() instanceof ServerPlayer vPlayer) || !(ctx.getLevel() instanceof ServerLevel vLevel))
            return vBlockState;

        Player player = Conversions.INSTANCE.snowball(vPlayer);
        World world = Conversions.INSTANCE.snowball(vLevel);
        Vector3i position = new Vector3i(ctx.getClickedPos().getX(), ctx.getClickedPos().getY(), ctx.getClickedPos().getZ());
        BlockState blockState = Conversions.INSTANCE.snowball(vBlockState);
        ItemStack itemStack = Conversions.INSTANCE.snowball(ctx.getItemInHand());
        Hand hand = Conversions.INSTANCE.snowball(ctx.getHand());

        PlayerBlockPlaceEvent.Allow allow = new PlayerBlockPlaceEventImpl.Allow(player, world, position, blockState, itemStack, hand);
        Snowball.getEventManager().dispatch(allow);

        if (!allow.getAllowed()) {
            PlayerBlockPlaceEvent.Canceled canceled = new PlayerBlockPlaceEventImpl.Canceled(player, world, position, blockState, itemStack, hand);
            Snowball.getEventManager().dispatch(canceled);

            cir.setReturnValue(InteractionResult.FAIL);
            return vBlockState;
        }

        // Make sure we get the item stack before it's modified by the action
        preItemStack.set(itemStack.copy());

        PlayerBlockPlaceEvent.Action action = new PlayerBlockPlaceEventImpl.Action(player, world, position, blockState, blockState, itemStack, hand);
        Snowball.getEventManager().dispatch(action);

        if (action.isDefaultPrevented()) {
            cir.setReturnValue(InteractionResult.FAIL);
            return vBlockState;
        }

        // Store the block state before the block is placed
        vPreviousBlockState.set(vLevel.getBlockState(ctx.getClickedPos()));

        return ((BlockStateImpl) action.getBlockState()).getAdaptee();
    }

    @Inject(method = "place", at = @At(value = "RETURN", ordinal = 5))
    public void inject$placeEventPost(
            BlockPlaceContext ctx, CallbackInfoReturnable<InteractionResult> cir,
            @Local(ordinal = 0) net.minecraft.world.level.block.state.BlockState vBlockState,
            @Share("previousBlockState") LocalRef<net.minecraft.world.level.block.state.BlockState> vPreviousBlockState,
            @Share("preItemStack") LocalRef<ItemStack> preItemStack
    ) {
        if (!(ctx.getPlayer() instanceof ServerPlayer vPlayer) || !(ctx.getLevel() instanceof ServerLevel vLevel))
            return;

        Player player = Conversions.INSTANCE.snowball(vPlayer);
        World world = Conversions.INSTANCE.snowball(vLevel);
        Vector3i position = new Vector3i(ctx.getClickedPos().getX(), ctx.getClickedPos().getY(), ctx.getClickedPos().getZ());
        BlockState blockState = Conversions.INSTANCE.snowball(vBlockState);
        BlockState previousBlockState = Conversions.INSTANCE.snowball(vPreviousBlockState.get());
        Hand hand = Conversions.INSTANCE.snowball(ctx.getHand());

        PlayerBlockPlaceEvent.Post allow = new PlayerBlockPlaceEventImpl.Post(player, world, position, blockState, previousBlockState, preItemStack.get(), hand);
        Snowball.getEventManager().dispatch(allow);
    }
}
