package io.github.pandier.snowball.math

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

    override fun plus(other: Vector3<Double>): Vector3d =
        Vector3d(x + other.x, y + other.y, z + other.z)

    override fun minus(other: Vector3<Double>): Vector3d =
        Vector3d(x - other.x, y - other.y, z - other.z)

    override fun times(scalar: Double): Vector3d =
        Vector3d(x * scalar, y * scalar, z * scalar)

    override fun div(scalar: Double): Vector3d =
        Vector3d(x / scalar, y / scalar, z / scalar)
}