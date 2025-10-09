package io.github.pandier.snowball.entity.player

import io.github.pandier.snowball.entity.EquipmentSlot

public enum class Hand(public val equipmentSlot: EquipmentSlot) {
    MAIN(EquipmentSlot.MAIN_HAND),
    OFF(EquipmentSlot.OFF_HAND)
}