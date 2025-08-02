package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.ServerPlayerEntityBridge;
import io.github.pandier.snowball.impl.entity.PlayerImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends EntityMixin implements ServerPlayerEntityBridge {
    @Unique private Text impl$joinMessage = null;

    @Override
    public PlayerImpl snowball$createAdapter() {
        return new PlayerImpl((ServerPlayerEntity) (Object) this);
    }

    @Override
    public void snowball$setJoinMessage(Text text) {
        this.impl$joinMessage = text;
    }

    @Override
    public Text snowball$getJoinMessage() {
        return this.impl$joinMessage;
    }
}
