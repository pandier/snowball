package io.github.pandier.snowball.impl

abstract class SnowballAdapter(
    open val adaptee: Any
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SnowballAdapter) return false
        return adaptee == other.adaptee
    }

    override fun hashCode(): Int {
        return adaptee.hashCode()
    }

    override fun toString(): String {
        return "SnowballAdapter($adaptee)"
    }
}