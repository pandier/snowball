package io.github.pandier.snowball.impl.entity.tracker

import net.kyori.adventure.audience.Audience
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.item.ItemEntity

class EntityDeathTracker {
    var message: Component? = null
    var audience: Audience = Audience.empty()
    val drops = mutableListOf<ItemEntity>()
    var experience: Int = 0

    fun addDrop(stack: ItemEntity) {
        this.drops.add(stack)
    }

    fun addExperience(amount: Int) {
        this.experience += amount
    }
}
