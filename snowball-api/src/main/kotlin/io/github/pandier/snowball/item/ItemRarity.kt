package io.github.pandier.snowball.item

import net.kyori.adventure.text.format.NamedTextColor

public enum class ItemRarity(
    public val identifier: String,
    public val color: NamedTextColor,
) {
    COMMON("common", NamedTextColor.WHITE),
    UNCOMMON("uncommon", NamedTextColor.YELLOW),
    RARE("rare", NamedTextColor.AQUA),
    EPIC("epic", NamedTextColor.LIGHT_PURPLE),
}
