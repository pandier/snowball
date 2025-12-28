package io.github.pandier.snowball.math

import kotlin.math.abs
import kotlin.math.sqrt

public data class Vector2l(
    public val x: Long,
    public val y: Long,
) {
    public constructor(a: Long) : this(a, a)

    public companion object Constants {
        @JvmField
        public val ZERO: Vector2l = Vector2l(0, 0)
    }

    public fun add(x: Long, y: Long): Vector2l =
        Vector2l(this.x + x, this.y + y)

    public fun add(other: Vector2l): Vector2l =
        add(other.x, other.y)

    public operator fun plus(other: Vector2l): Vector2l =
        add(other.x, other.y)

    public fun sub(x: Long, y: Long): Vector2l =
        Vector2l(this.x - x, this.y - y)

    public fun sub(other: Vector2l): Vector2l =
        sub(other.x, other.y)

    public operator fun minus(other: Vector2l): Vector2l =
        sub(other.x, other.y)

    public fun mul(x: Long, y: Long): Vector2l =
        Vector2l(this.x * x, this.y * y)

    public fun mul(other: Vector2l): Vector2l =
        mul(other.x, other.y)

    public operator fun times(other: Vector2l): Vector2l =
        mul(other.x, other.y)

    public fun mul(scalar: Long): Vector2l =
        Vector2l(x * scalar, y * scalar)

    public operator fun times(scalar: Long): Vector2l =
        mul(scalar)

    public fun div(x: Long, y: Long): Vector2l =
        Vector2l(this.x / x, this.y / y)

    public operator fun div(other: Vector2l): Vector2l =
        div(other.x, other.y)

    public operator fun div(scalar: Long): Vector2l =
        Vector2l(x / scalar, y / scalar)

    public fun min(x: Long, y: Long): Vector2l =
        Vector2l(minOf(this.x, x), minOf(this.y, y))

    public fun min(other: Vector2l): Vector2l =
        min(other.x, other.y)

    public fun max(x: Long, y: Long): Vector2l =
        Vector2l(maxOf(this.x, x), maxOf(this.y, y))

    public fun max(other: Vector2l): Vector2l =
        max(other.x, other.y)

    public fun distanceSquared(other: Vector2l): Long =
        (this - other).lengthSquared()

    public fun distance(other: Vector2l): Double =
        (this - other).length()

    public fun lengthSquared(): Long =
        x * x + y * y

    public fun length(): Double =
        sqrt(lengthSquared().toDouble())

    public fun absolute(): Vector2l =
        Vector2l(abs(x), abs(y))

    public fun negate(): Vector2l =
        Vector2l(-x, -y)

    public fun toVector2i(): Vector2i =
        Vector2i(x.toInt(), y.toInt())

    public fun toVector2f(): Vector2f =
        Vector2f(x.toFloat(), y.toFloat())

    public fun toVector2d(): Vector2d =
        Vector2d(x.toDouble(), y.toDouble())

}
