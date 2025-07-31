package io.github.pandier.snowball.impl.adventure

import net.kyori.adventure.text.Component
import net.minecraft.text.MutableText
import net.minecraft.text.OrderedText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.text.TextContent

class AdventureText(val adventure: Component) : Text {
    private val vanilla: Text by lazy { VanillaComponentSerializer.serialize(adventure) }
    override fun getStyle(): Style = vanilla.style
    override fun getContent(): TextContent = vanilla.content
    override fun getSiblings(): List<Text> = vanilla.siblings
    override fun asOrderedText(): OrderedText = vanilla.asOrderedText()
    override fun copyContentOnly(): MutableText = vanilla.copyContentOnly()
    override fun copy(): MutableText = vanilla.copy()
    override fun equals(other: Any?): Boolean = vanilla == other
    override fun hashCode(): Int = vanilla.hashCode()
}