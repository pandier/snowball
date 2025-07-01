package io.github.pandier.snowball.math

public data class Vector3i(
    override val x: Int,
    override val y: Int,
    override val z: Int
) : Vector3<Int> {
    public constructor(a: Int) : this(a, a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector3i = Vector3i(0, 0, 0)
    }

    override fun plus(other: Vector3<Int>): Vector3i =
        Vector3i(x + other.x, y + other.y, z + other.z)

    override fun minus(other: Vector3<Int>): Vector3i =
        Vector3i(x - other.x, y - other.y, z - other.z)

    override fun times(scalar: Int): Vector3i =
        Vector3i(x * scalar, y * scalar, z * scalar)

    override fun div(scalar: Int): Vector3i =
        Vector3i(x / scalar, y / scalar, z / scalar)
}