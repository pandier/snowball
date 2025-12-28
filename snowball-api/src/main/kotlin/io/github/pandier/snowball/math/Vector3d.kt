package io.github.pandier.snowball.math

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

public data class Vector3d(
    public val x: Double,
    public val y: Double,
    public val z: Double,
) {
    public constructor(a: Double) : this(a, a, a)

    public companion object Constants {
        @JvmField
        public val ZERO: Vector3d = Vector3d(0.0, 0.0, 0.0)
    }

    public fun add(x: Double, y: Double, z: Double): Vector3d =
        Vector3d(this.x + x, this.y + y, this.z + z)

    public fun add(other: Vector3d): Vector3d =
        add(other.x, other.y, other.z)

    public operator fun plus(other: Vector3d): Vector3d =
        add(other.x, other.y, other.z)

    public fun sub(x: Double, y: Double, z: Double): Vector3d =
        Vector3d(this.x - x, this.y - y, this.z - z)

    public fun sub(other: Vector3d): Vector3d =
        sub(other.x, other.y, other.z)

    public operator fun minus(other: Vector3d): Vector3d =
        sub(other.x, other.y, other.z)

    public fun mul(x: Double, y: Double, z: Double): Vector3d =
        Vector3d(this.x * x, this.y * y, this.z * z)

    public fun mul(other: Vector3d): Vector3d =
        mul(other.x, other.y, other.z)

    public operator fun times(other: Vector3d): Vector3d =
        mul(other.x, other.y, other.z)

    public fun mul(scalar: Double): Vector3d =
        Vector3d(x * scalar, y * scalar, z * scalar)

    public operator fun times(scalar: Double): Vector3d =
        mul(scalar)

    public fun div(x: Double, y: Double, z: Double): Vector3d =
        Vector3d(this.x / x, this.y / y, this.z / z)

    public operator fun div(other: Vector3d): Vector3d =
        div(other.x, other.y, other.z)

    public operator fun div(scalar: Double): Vector3d =
        Vector3d(x / scalar, y / scalar, z / scalar)

    public fun min(x: Double, y: Double, z: Double): Vector3d =
        Vector3d(minOf(this.x, x), minOf(this.y, y), minOf(this.z, z))

    public fun min(other: Vector3d): Vector3d =
        min(other.x, other.y, other.z)

    public fun max(x: Double, y: Double, z: Double): Vector3d =
        Vector3d(maxOf(this.x, x), maxOf(this.y, y), maxOf(this.z, z))

    public fun max(other: Vector3d): Vector3d =
        max(other.x, other.y, other.z)

    public fun floor(): Vector3d =
        Vector3d(floor(x), floor(y), floor(z))

    public fun ceil(): Vector3d =
        Vector3d(ceil(x), ceil(y), ceil(z))

    public fun rotateX(angle: Double): Vector3d {
        val cos = cos(angle)
        val sin = sin(angle)
        return Vector3d(this.x, this.y * cos + this.z * sin, this.z * cos - this.y * sin)
    }

    public fun rotateY(angle: Double): Vector3d {
        val cos = cos(angle)
        val sin = sin(angle)
        return Vector3d(this.x * cos + this.z * sin, this.y, this.z * cos - this.x * sin)
    }

    public fun rotateZ(angle: Double): Vector3d {
        val cos = cos(angle)
        val sin = sin(angle)
        return Vector3d(this.x * cos + this.y * sin, this.y * cos - this.x * sin, this.z)
    }

    public fun distanceSquared(other: Vector3d): Double =
        (this - other).lengthSquared()

    public fun distance(other: Vector3d): Double =
        (this - other).length()

    public fun lengthSquared(): Double =
        x * x + y * y + z * z

    public fun length(): Double =
        sqrt(lengthSquared())

    public fun absolute(): Vector3d =
        Vector3d(abs(x), abs(y), abs(z))

    public fun negate(): Vector3d =
        Vector3d(-x, -y, -z)

    public fun toVector3i(): Vector3i =
        Vector3i(x.toInt(), y.toInt(), z.toInt())

    public fun toVector3l(): Vector3l =
        Vector3l(x.toLong(), y.toLong(), z.toLong())

    public fun toVector3f(): Vector3f =
        Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

    public fun toBlockPos(): Vector3i =
        floor().toVector3i()

}
