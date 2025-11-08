package io.github.pandier.snowball.impl.mixin;

import io.github.pandier.snowball.impl.entity.EntityImpl;
import io.github.pandier.snowball.impl.entity.ItemEntityImpl;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin {

    @Override
    public EntityImpl snowball$createAdapter() {
        return new ItemEntityImpl((ItemEntity) (Object) this);
    }
}
