package io.github.pandier.snowball.math

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

public data class Vector2f(
    public val x: Float,
    public val y: Float,
) {
    public constructor(a: Float) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2f = Vector2f(0f, 0f)
    }

    public fun add(x: Float, y: Float): Vector2f =
        Vector2f(this.x + x, this.y + y)

    public fun add(other: Vector2f): Vector2f =
        add(other.x, other.y)

    public operator fun plus(other: Vector2f): Vector2f =
        add(other.x, other.y)

    public fun sub(x: Float, y: Float): Vector2f =
        Vector2f(this.x - x, this.y - y)

    public fun sub(other: Vector2f): Vector2f =
        sub(other.x, other.y)

    public operator fun minus(other: Vector2f): Vector2f =
        sub(other.x, other.y)

    public fun mul(x: Float, y: Float): Vector2f =
        Vector2f(this.x * x, this.y * y)

    public fun mul(other: Vector2f): Vector2f =
        mul(other.x, other.y)

    public operator fun times(other: Vector2f): Vector2f =
        mul(other.x, other.y)

    public fun mul(scalar: Float): Vector2f =
        Vector2f(x * scalar, y * scalar)

    public operator fun times(scalar: Float): Vector2f =
        mul(scalar)

    public fun div(x: Float, y: Float): Vector2f =
        Vector2f(this.x / x, this.y / y)

    public operator fun div(other: Vector2f): Vector2f =
        div(other.x, other.y)

    public operator fun div(scalar: Float): Vector2f =
        Vector2f(x / scalar, y / scalar)

    public fun min(x: Float, y: Float): Vector2f =
        Vector2f(minOf(this.x, x), minOf(this.y, y))

    public fun min(other: Vector2f): Vector2f =
        min(other.x, other.y)

    public fun max(x: Float, y: Float): Vector2f =
        Vector2f(maxOf(this.x, x), maxOf(this.y, y))

    public fun max(other: Vector2f): Vector2f =
        max(other.x, other.y)

    public fun floor(): Vector2f =
        Vector2f(floor(x), floor(y))

    public fun ceil(): Vector2f =
        Vector2f(ceil(x), ceil(y))

    public fun distanceSquared(other: Vector2f): Double =
        (this - other).lengthSquared()

    public fun distance(other: Vector2f): Double =
        (this - other).length()

    public fun lengthSquared(): Double =
        x.toDouble() * x + y.toDouble() * y

    public fun length(): Double =
        sqrt(lengthSquared())

    public fun absolute(): Vector2f =
        Vector2f(abs(x), abs(y))

    public fun negate(): Vector2f =
        Vector2f(-x, -y)

    public fun toVector2i(): Vector2i =
        Vector2i(x.toInt(), y.toInt())

    public fun toVector2l(): Vector2l =
        Vector2l(x.toLong(), y.toLong())

    public fun toVector2d(): Vector2d =
        Vector2d(x.toDouble(), y.toDouble())

}
