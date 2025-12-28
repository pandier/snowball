package io.github.pandier.snowball.math

import kotlin.math.abs
import kotlin.math.sqrt

public data class Vector3i(
    public val x: Int,
    public val y: Int,
    public val z: Int,
) {
    public constructor(a: Int) : this(a, a, a)

    public companion object Constants {
        @JvmField
        public val ZERO: Vector3i = Vector3i(0, 0, 0)
    }

    public fun add(x: Int, y: Int, z: Int): Vector3i =
        Vector3i(this.x + x, this.y + y, this.z + z)

    public fun add(other: Vector3i): Vector3i =
        add(other.x, other.y, other.z)

    public operator fun plus(other: Vector3i): Vector3i =
        add(other.x, other.y, other.z)

    public fun sub(x: Int, y: Int, z: Int): Vector3i =
        Vector3i(this.x - x, this.y - y, this.z - z)

    public fun sub(other: Vector3i): Vector3i =
        sub(other.x, other.y, other.z)

    public operator fun minus(other: Vector3i): Vector3i =
        sub(other.x, other.y, other.z)

    public fun mul(x: Int, y: Int, z: Int): Vector3i =
        Vector3i(this.x * x, this.y * y, this.z * z)

    public fun mul(other: Vector3i): Vector3i =
        mul(other.x, other.y, other.z)

    public operator fun times(other: Vector3i): Vector3i =
        mul(other.x, other.y, other.z)

    public fun mul(scalar: Int): Vector3i =
        Vector3i(x * scalar, y * scalar, z * scalar)

    public operator fun times(scalar: Int): Vector3i =
        mul(scalar)

    public fun div(x: Int, y: Int, z: Int): Vector3i =
        Vector3i(this.x / x, this.y / y, this.z / z)

    public operator fun div(other: Vector3i): Vector3i =
        div(other.x, other.y, other.z)

    public operator fun div(scalar: Int): Vector3i =
        Vector3i(x / scalar, y / scalar, z / scalar)

    public fun min(x: Int, y: Int, z: Int): Vector3i =
        Vector3i(minOf(this.x, x), minOf(this.y, y), minOf(this.z, z))

    public fun min(other: Vector3i): Vector3i =
        min(other.x, other.y, other.z)

    public fun max(x: Int, y: Int, z: Int): Vector3i =
        Vector3i(maxOf(this.x, x), maxOf(this.y, y), maxOf(this.z, z))

    public fun max(other: Vector3i): Vector3i =
        max(other.x, other.y, other.z)

    public fun distanceSquared(other: Vector3i): Long =
        (this - other).lengthSquared()

    public fun distance(other: Vector3i): Double =
        (this - other).length()

    public fun lengthSquared(): Long =
        x.toLong() * x + y.toLong() * y + z.toLong() * z

    public fun length(): Double =
        sqrt(lengthSquared().toDouble())

    public fun absolute(): Vector3i =
        Vector3i(abs(x), abs(y), abs(z))

    public fun negate(): Vector3i =
        Vector3i(-x, -y, -z)

    public fun toVector3l(): Vector3l =
        Vector3l(x.toLong(), y.toLong(), z.toLong())

    public fun toVector3f(): Vector3f =
        Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

    public fun toVector3d(): Vector3d =
        Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

}
