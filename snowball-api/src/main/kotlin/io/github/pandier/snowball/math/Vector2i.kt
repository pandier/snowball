package io.github.pandier.snowball.math

import kotlin.math.abs
import kotlin.math.sqrt

public data class Vector2i(
    public val x: Int,
    public val y: Int,
) {
    public constructor(a: Int) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2i = Vector2i(0, 0)
    }

    public fun add(x: Int, y: Int): Vector2i =
        Vector2i(this.x + x, this.y + y)

    public fun add(other: Vector2i): Vector2i =
        add(other.x, other.y)

    public operator fun plus(other: Vector2i): Vector2i =
        add(other.x, other.y)

    public fun sub(x: Int, y: Int): Vector2i =
        Vector2i(this.x - x, this.y - y)

    public fun sub(other: Vector2i): Vector2i =
        sub(other.x, other.y)

    public operator fun minus(other: Vector2i): Vector2i =
        sub(other.x, other.y)

    public fun mul(x: Int, y: Int): Vector2i =
        Vector2i(this.x * x, this.y * y)

    public fun mul(other: Vector2i): Vector2i =
        mul(other.x, other.y)

    public operator fun times(other: Vector2i): Vector2i =
        mul(other.x, other.y)

    public fun mul(scalar: Int): Vector2i =
        Vector2i(x * scalar, y * scalar)

    public operator fun times(scalar: Int): Vector2i =
        mul(scalar)

    public fun div(x: Int, y: Int): Vector2i =
        Vector2i(this.x / x, this.y / y)

    public operator fun div(other: Vector2i): Vector2i =
        div(other.x, other.y)

    public operator fun div(scalar: Int): Vector2i =
        Vector2i(x / scalar, y / scalar)

    public fun min(x: Int, y: Int): Vector2i =
        Vector2i(minOf(this.x, x), minOf(this.y, y))

    public fun min(other: Vector2i): Vector2i =
        min(other.x, other.y)

    public fun max(x: Int, y: Int): Vector2i =
        Vector2i(maxOf(this.x, x), maxOf(this.y, y))

    public fun max(other: Vector2i): Vector2i =
        max(other.x, other.y)

    public fun distanceSquared(other: Vector2i): Long =
        (this - other).lengthSquared()

    public fun distance(other: Vector2i): Double =
        (this - other).length()

    public fun lengthSquared(): Long =
        x.toLong() * x + y.toLong() * y

    public fun length(): Double =
        sqrt(lengthSquared().toDouble())

    public fun absolute(): Vector2i =
        Vector2i(abs(x), abs(y))

    public fun negate(): Vector2i =
        Vector2i(-x, -y)

    public fun toVector2l(): Vector2l =
        Vector2l(x.toLong(), y.toLong())

    public fun toVector2f(): Vector2f =
        Vector2f(x.toFloat(), y.toFloat())

    public fun toVector2d(): Vector2d =
        Vector2d(x.toDouble(), y.toDouble())

}
