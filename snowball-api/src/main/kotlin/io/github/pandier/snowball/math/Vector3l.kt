package io.github.pandier.snowball.math

import kotlin.math.abs
import kotlin.math.sqrt

public data class Vector3l(
    public val x: Long,
    public val y: Long,
    public val z: Long,
) {
    public constructor(a: Long) : this(a, a, a)

    public companion object Constants {
        @JvmField
        public val ZERO: Vector3l = Vector3l(0, 0, 0)
    }

    public fun add(x: Long, y: Long, z: Long): Vector3l =
        Vector3l(this.x + x, this.y + y, this.z + z)

    public fun add(other: Vector3l): Vector3l =
        add(other.x, other.y, other.z)

    public operator fun plus(other: Vector3l): Vector3l =
        add(other.x, other.y, other.z)

    public fun sub(x: Long, y: Long, z: Long): Vector3l =
        Vector3l(this.x - x, this.y - y, this.z - z)

    public fun sub(other: Vector3l): Vector3l =
        sub(other.x, other.y, other.z)

    public operator fun minus(other: Vector3l): Vector3l =
        sub(other.x, other.y, other.z)

    public fun mul(x: Long, y: Long, z: Long): Vector3l =
        Vector3l(this.x * x, this.y * y, this.z * z)

    public fun mul(other: Vector3l): Vector3l =
        mul(other.x, other.y, other.z)

    public operator fun times(other: Vector3l): Vector3l =
        mul(other.x, other.y, other.z)

    public fun mul(scalar: Long): Vector3l =
        Vector3l(x * scalar, y * scalar, z * scalar)

    public operator fun times(scalar: Long): Vector3l =
        mul(scalar)

    public fun div(x: Long, y: Long, z: Long): Vector3l =
        Vector3l(this.x / x, this.y / y, this.z / z)

    public operator fun div(other: Vector3l): Vector3l =
        div(other.x, other.y, other.z)

    public operator fun div(scalar: Long): Vector3l =
        Vector3l(x / scalar, y / scalar, z / scalar)

    public fun min(x: Long, y: Long, z: Long): Vector3l =
        Vector3l(minOf(this.x, x), minOf(this.y, y), minOf(this.z, z))

    public fun min(other: Vector3l): Vector3l =
        min(other.x, other.y, other.z)

    public fun max(x: Long, y: Long, z: Long): Vector3l =
        Vector3l(maxOf(this.x, x), maxOf(this.y, y), maxOf(this.z, z))

    public fun max(other: Vector3l): Vector3l =
        max(other.x, other.y, other.z)

    public fun distanceSquared(other: Vector3l): Long =
        (this - other).lengthSquared()

    public fun distance(other: Vector3l): Double =
        (this - other).length()

    public fun lengthSquared(): Long =
        x * x + y * y + z * z

    public fun length(): Double =
        sqrt(lengthSquared().toDouble())

    public fun absolute(): Vector3l =
        Vector3l(abs(x), abs(y), abs(z))

    public fun negate(): Vector3l =
        Vector3l(-x, -y, -z)

    public fun toVector3i(): Vector3i =
        Vector3i(x.toInt(), y.toInt(), z.toInt())

    public fun toVector3f(): Vector3f =
        Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

    public fun toVector3d(): Vector3d =
        Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

}
