package io.github.pandier.snowball.impl.event

import io.github.pandier.snowball.event.AllowEvent

abstract class AllowEventImpl : AllowEvent {
    override var allowed: Boolean = true
}