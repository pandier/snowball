package io.github.pandier.snowball.impl.event

import io.github.pandier.snowball.event.ActionEvent

abstract class ActionEventImpl : ActionEvent {
    override var isDefaultPrevented: Boolean = false
}