package io.github.pandier.snowball.math

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil

public data class Vector2d(
    public val x: Double,
    public val y: Double,
) {
    public constructor(a: Double) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2d = Vector2d(0.0, 0.0)
    }

    public fun add(x: Double, y: Double): Vector2d =
        Vector2d(this.x + x, this.y + y)

    public fun add(other: Vector2d): Vector2d =
        add(other.x, other.y)

    public operator fun plus(other: Vector2d): Vector2d =
        add(other.x, other.y)

    public fun sub(x: Double, y: Double): Vector2d =
        Vector2d(this.x - x, this.y - y)

    public fun sub(other: Vector2d): Vector2d =
        sub(other.x, other.y)

    public operator fun minus(other: Vector2d): Vector2d =
        sub(other.x, other.y)

    public fun mul(x: Double, y: Double): Vector2d =
        Vector2d(this.x * x, this.y * y)

    public fun mul(other: Vector2d): Vector2d =
        mul(other.x, other.y)

    public operator fun times(other: Vector2d): Vector2d =
        mul(other.x, other.y)

    public fun mul(scalar: Double): Vector2d =
        Vector2d(x * scalar, y * scalar)

    public operator fun times(scalar: Double): Vector2d =
        mul(scalar)

    public fun div(x: Double, y: Double): Vector2d =
        Vector2d(this.x / x, this.y / y)

    public operator fun div(other: Vector2d): Vector2d =
        div(other.x, other.y)

    public operator fun div(scalar: Double): Vector2d =
        Vector2d(x / scalar, y / scalar)

    public fun min(x: Double, y: Double): Vector2d =
        Vector2d(minOf(this.x, x), minOf(this.y, y))

    public fun min(other: Vector2d): Vector2d =
        min(other.x, other.y)

    public fun max(x: Double, y: Double): Vector2d =
        Vector2d(maxOf(this.x, x), maxOf(this.y, y))

    public fun max(other: Vector2d): Vector2d =
        max(other.x, other.y)

    public fun floor(): Vector2d =
        Vector2d(floor(x), floor(y))

    public fun ceil(): Vector2d =
        Vector2d(ceil(x), ceil(y))

    public fun distanceSquared(other: Vector2d): Double =
        (this - other).lengthSquared()

    public fun distance(other: Vector2d): Double =
        (this - other).length()

    public fun lengthSquared(): Double =
        x * x + y * y

    public fun length(): Double =
        sqrt(lengthSquared())

    public fun absolute(): Vector2d =
        Vector2d(abs(x), abs(y))

    public fun negate(): Vector2d =
        Vector2d(-x, -y)

    public fun toVector2i(): Vector2i =
        Vector2i(x.toInt(), y.toInt())

    public fun toVector2l(): Vector2l =
        Vector2l(x.toLong(), y.toLong())

    public fun toVector2f(): Vector2f =
        Vector2f(x.toFloat(), y.toFloat())

}
