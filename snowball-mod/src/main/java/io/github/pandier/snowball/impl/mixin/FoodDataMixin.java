package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.bridge.FoodDataBridge;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodData.class)
public class FoodDataMixin implements FoodDataBridge {
    @Shadow private float exhaustionLevel;

    @Override
    public void snowball$setExhaustion(float exhaustion) {
        this.exhaustionLevel = exhaustion;
    }

    @Override
    public float snowball$getExhaustion() {
        return this.exhaustionLevel;
    }
}
