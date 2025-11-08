package io.github.pandier.snowball.impl.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.pandier.snowball.impl.Conversions;
import io.github.pandier.snowball.impl.entity.tracker.EntityDeathTracker;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.TamableAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin extends MobMixin {

    @WrapOperation(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;sendSystemMessage(Lnet/minecraft/network/chat/Component;)V"))
    public void inject$die$trackDeathMessage(ServerPlayer player, Component component, Operation<Void> original) {
        EntityDeathTracker tracker = this.snowball$startDeathTracker();
        if (tracker != null) {
            tracker.setMessage(component);
            tracker.setAudience(Conversions.INSTANCE.snowball(player));
        } else {
            original.call(player, component);
        }
    }
}
