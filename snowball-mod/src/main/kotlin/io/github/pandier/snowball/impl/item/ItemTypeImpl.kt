package io.github.pandier.snowball.impl.item

import io.github.pandier.snowball.impl.adapter.SnowballAdapter
import io.github.pandier.snowball.item.ItemType
import net.minecraft.world.item.Item

class ItemTypeImpl(
    override val adaptee: Item
) : SnowballAdapter(adaptee), ItemType
