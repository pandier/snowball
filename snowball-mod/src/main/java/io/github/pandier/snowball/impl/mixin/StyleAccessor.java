package io.github.pandier.snowball.impl.mixin;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Style.class)
public interface StyleAccessor {

    @Invoker("<init>")
    static Style snowball$init(
        @Nullable TextColor color,
		@Nullable Integer shadowColor,
		@Nullable Boolean bold,
		@Nullable Boolean italic,
		@Nullable Boolean underlined,
		@Nullable Boolean strikethrough,
		@Nullable Boolean obfuscated,
		@Nullable ClickEvent clickEvent,
		@Nullable HoverEvent hoverEvent,
		@Nullable String insertion,
		@Nullable Identifier font
    ) {
        throw new AssertionError();
    }

	@Accessor("bold")
	@Nullable Boolean snowball$getBold();

	@Accessor("italic")
	@Nullable Boolean snowball$getItalic();

	@Accessor("underlined")
	@Nullable Boolean snowball$getUnderlined();

	@Accessor("strikethrough")
	@Nullable Boolean snowball$getStrikethrough();

	@Accessor("obfuscated")
	@Nullable Boolean snowball$getObfuscated();
}
