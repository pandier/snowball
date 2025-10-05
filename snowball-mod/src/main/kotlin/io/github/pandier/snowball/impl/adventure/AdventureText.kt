package io.github.pandier.snowball.impl.adventure

import net.kyori.adventure.text.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.util.FormattedCharSequence

class AdventureText(val adventure: Component) : net.minecraft.network.chat.Component {
    private val vanilla: net.minecraft.network.chat.Component by lazy { VanillaComponentSerializer.serialize(adventure) }
    override fun getStyle(): Style = vanilla.style
    override fun getContents(): net.minecraft.network.chat.ComponentContents = vanilla.contents
    override fun getSiblings(): List<net.minecraft.network.chat.Component> = vanilla.siblings
    override fun getVisualOrderText(): FormattedCharSequence = vanilla.visualOrderText
    override fun plainCopy(): MutableComponent = vanilla.plainCopy()
    override fun copy(): MutableComponent = vanilla.copy()
    override fun equals(other: Any?): Boolean = vanilla == other
    override fun hashCode(): Int = vanilla.hashCode()
}