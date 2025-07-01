package io.github.pandier.snowball.math

public data class Vector3l(
    override val x: Long,
    override val y: Long,
    override val z: Long
) : Vector3<Long> {
    public constructor(a: Long) : this(a, a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector3l = Vector3l(0L, 0L, 0L)
    }

    override fun plus(other: Vector3<Long>): Vector3l =
        Vector3l(x + other.x, y + other.y, z + other.z)

    override fun minus(other: Vector3<Long>): Vector3l =
        Vector3l(x - other.x, y - other.y, z - other.z)

    override fun times(scalar: Long): Vector3l =
        Vector3l(x * scalar, y * scalar, z * scalar)

    override fun div(scalar: Long): Vector3l =
        Vector3l(x / scalar, y / scalar, z / scalar)
}