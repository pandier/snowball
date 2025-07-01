package io.github.pandier.snowball.math

public data class Vector3f(
    override val x: Float,
    override val y: Float,
    override val z: Float
) : Vector3<Float> {
    public constructor(a: Float) : this(a, a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector3f = Vector3f(0f, 0f, 0f)
    }

    override fun plus(other: Vector3<Float>): Vector3f =
        Vector3f(x + other.x, y + other.y, z + other.z)

    override fun minus(other: Vector3<Float>): Vector3f =
        Vector3f(x - other.x, y - other.y, z - other.z)

    override fun times(scalar: Float): Vector3f =
        Vector3f(x * scalar, y * scalar, z * scalar)

    override fun div(scalar: Float): Vector3f =
        Vector3f(x / scalar, y / scalar, z / scalar)
}