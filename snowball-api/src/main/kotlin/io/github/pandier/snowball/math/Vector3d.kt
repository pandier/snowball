package io.github.pandier.snowball.math

import kotlin.math.floor
import kotlin.math.ceil

public data class Vector3d(
    override val x: Double,
    override val y: Double,
    override val z: Double
) : Vector3<Double> {
    public constructor(a: Double) : this(a, a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector3d = Vector3d(0.0, 0.0, 0.0)
    }

    public fun plus(x: Double, y: Double, z: Double): Vector3d =
        Vector3d(this.x + x, this.y + y, this.z + z)

    override fun plus(other: Vector3<Double>): Vector3d =
        plus(other.x, other.y, other.z)

    public fun minus(x: Double, y: Double, z: Double): Vector3d =
        Vector3d(this.x - x, this.y - y, this.z - z)

    override fun minus(other: Vector3<Double>): Vector3d =
        Vector3d(x - other.x, y - other.y, z - other.z)

    override fun times(scalar: Double): Vector3d =
        Vector3d(x * scalar, y * scalar, z * scalar)

    override fun div(scalar: Double): Vector3d =
        Vector3d(x / scalar, y / scalar, z / scalar)

    public fun floor(): Vector3d =
        Vector3d(floor(x), floor(y), floor(z))

    public fun ceil(): Vector3d =
        Vector3d(ceil(x), ceil(y), ceil(z))

    public fun toVector3i(): Vector3i =
        Vector3i(x.toInt(), y.toInt(), z.toInt())

    public fun toVector3l(): Vector3l =
        Vector3l(x.toLong(), y.toLong(), z.toLong())

    public fun toVector3f(): Vector3f =
        Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

    public fun toBlockPos(): Vector3i =
        floor().toVector3i()
}