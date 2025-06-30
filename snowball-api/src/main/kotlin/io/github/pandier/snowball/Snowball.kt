package io.github.pandier.snowball

import io.github.pandier.snowball.event.EventManager

interface Snowball {
    companion object Holder {
        private lateinit var instance: Snowball

        @JvmStatic
        fun get(): Snowball = instance
    }

    val eventManager: EventManager
}