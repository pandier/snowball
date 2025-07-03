package io.github.pandier.snowball.impl.mixin;

import net.minecraft.text.TextColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextColor.class)
public interface TextColorAccessor {

    @Accessor("name")
    @Nullable String snowball$getName();
}
