package io.github.pandier.snowball.math

import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.floor
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

public data class Vector3f(
    public val x: Float,
    public val y: Float,
    public val z: Float,
) {
    public constructor(a: Float) : this(a, a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector3f = Vector3f(0f, 0f, 0f)
    }

    public fun add(x: Float, y: Float, z: Float): Vector3f =
        Vector3f(this.x + x, this.y + y, this.z + z)

    public fun add(other: Vector3f): Vector3f =
        add(other.x, other.y, other.z)

    public operator fun plus(other: Vector3f): Vector3f =
        add(other.x, other.y, other.z)

    public fun sub(x: Float, y: Float, z: Float): Vector3f =
        Vector3f(this.x - x, this.y - y, this.z - z)

    public fun sub(other: Vector3f): Vector3f =
        sub(other.x, other.y, other.z)

    public operator fun minus(other: Vector3f): Vector3f =
        sub(other.x, other.y, other.z)

    public fun mul(x: Float, y: Float, z: Float): Vector3f =
        Vector3f(this.x * x, this.y * y, this.z * z)

    public fun mul(other: Vector3f): Vector3f =
        mul(other.x, other.y, other.z)

    public operator fun times(other: Vector3f): Vector3f =
        mul(other.x, other.y, other.z)

    public fun mul(scalar: Float): Vector3f =
        Vector3f(x * scalar, y * scalar, z * scalar)

    public operator fun times(scalar: Float): Vector3f =
        mul(scalar)

    public fun div(x: Float, y: Float, z: Float): Vector3f =
        Vector3f(this.x / x, this.y / y, this.z / z)

    public operator fun div(other: Vector3f): Vector3f =
        div(other.x, other.y, other.z)

    public operator fun div(scalar: Float): Vector3f =
        Vector3f(x / scalar, y / scalar, z / scalar)

    public fun min(x: Float, y: Float, z: Float): Vector3f =
        Vector3f(minOf(this.x, x), minOf(this.y, y), minOf(this.z, z))

    public fun min(other: Vector3f): Vector3f =
        min(other.x, other.y, other.z)

    public fun max(x: Float, y: Float, z: Float): Vector3f =
        Vector3f(maxOf(this.x, x), maxOf(this.y, y), maxOf(this.z, z))

    public fun max(other: Vector3f): Vector3f =
        max(other.x, other.y, other.z)

    public fun floor(): Vector3f =
        Vector3f(floor(x), floor(y), floor(z))

    public fun ceil(): Vector3f =
        Vector3f(ceil(x), ceil(y), ceil(z))

    public fun rotateX(angle: Float): Vector3f {
        val cos = cos(angle)
        val sin = sin(angle)
        return Vector3f(this.x, this.y * cos + this.z * sin, this.z * cos - this.y * sin)
    }

    public fun rotateY(angle: Float): Vector3f {
        val cos = cos(angle)
        val sin = sin(angle)
        return Vector3f(this.x * cos + this.z * sin, this.y, this.z * cos - this.x * sin)
    }

    public fun rotateZ(angle: Float): Vector3f {
        val cos = cos(angle)
        val sin = sin(angle)
        return Vector3f(this.x * cos + this.y * sin, this.y * cos - this.x * sin, this.z)
    }

    public fun distanceSquared(other: Vector3f): Double =
        (this - other).lengthSquared()

    public fun distance(other: Vector3f): Double =
        (this - other).length()

    public fun lengthSquared(): Double =
        x.toDouble() * x + y.toDouble() * y + z.toDouble() * z

    public fun length(): Double =
        sqrt(lengthSquared())

    public fun absolute(): Vector3f =
        Vector3f(abs(x), abs(y), abs(z))

    public fun negate(): Vector3f =
        Vector3f(-x, -y, -z)

    public fun toVector3i(): Vector3i =
        Vector3i(x.toInt(), y.toInt(), z.toInt())

    public fun toVector3l(): Vector3l =
        Vector3l(x.toLong(), y.toLong(), z.toLong())

    public fun toVector3d(): Vector3d =
        Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

    public fun toBlockPos(): Vector3i =
        floor().toVector3i()

}
