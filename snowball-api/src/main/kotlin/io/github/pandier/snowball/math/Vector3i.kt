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

    public fun plus(x: Int, y: Int, z: Int): Vector3i =
        Vector3i(this.x + x, this.y + y, this.z + z)

    override fun plus(other: Vector3<Int>): Vector3i =
        plus(other.x, other.y, other.z)

    public fun minus(x: Int, y: Int, z: Int): Vector3i =
        Vector3i(this.x - x, this.y - y, this.z - z)

    override fun minus(other: Vector3<Int>): Vector3i =
        Vector3i(x - other.x, y - other.y, z - other.z)

    override fun times(scalar: Int): Vector3i =
        Vector3i(x * scalar, y * scalar, z * scalar)

    override fun div(scalar: Int): Vector3i =
        Vector3i(x / scalar, y / scalar, z / scalar)

    public fun toVector3l(): Vector3l =
        Vector3l(x.toLong(), y.toLong(), z.toLong())

    public fun toVector3f(): Vector3f =
        Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

    public fun toVector3d(): Vector3d =
        Vector3d(x.toDouble(), y.toDouble(), z.toDouble())
}