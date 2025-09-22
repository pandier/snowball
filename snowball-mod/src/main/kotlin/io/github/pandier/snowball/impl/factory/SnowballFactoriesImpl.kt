package io.github.pandier.snowball.impl.factory

import io.github.pandier.snowball.factory.SnowballFactories
import io.github.pandier.snowball.impl.item.ItemStackImpl
import io.github.pandier.snowball.impl.item.ItemTypeImpl
import io.github.pandier.snowball.item.ItemStack
import io.github.pandier.snowball.item.ItemStackView
import io.github.pandier.snowball.item.ItemType

class SnowballFactoriesImpl : SnowballFactories {
    override fun emptyItemStack(): ItemStackView {
        return ItemStackImpl.EMPTY
    }

    override fun itemStack(type: ItemType, count: Int): ItemStack {
        return ItemStackImpl(net.minecraft.item.ItemStack((type as ItemTypeImpl).adaptee, count))
    }
}