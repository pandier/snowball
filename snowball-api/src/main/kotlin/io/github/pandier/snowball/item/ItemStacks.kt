package io.github.pandier.snowball.item

public object ItemStacks {
    @JvmStatic
    public fun canStack(a: ItemStackView, b: ItemStackView): Boolean {
        return !a.isEmpty() && a.type == b.type && a.components == b.components && a.isStackable
    }
}