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

    public fun plus(x: Float, y: Float, z: Float): Vector3f =
        Vector3f(this.x + x, this.y + y, this.z + z)

    override fun plus(other: Vector3<Float>): Vector3f =
        plus(other.x, other.y, other.z)

    public fun minus(x: Float, y: Float, z: Float): Vector3f =
        Vector3f(this.x - x, this.y - y, this.z - z)

    override fun minus(other: Vector3<Float>): Vector3f =
        Vector3f(x - other.x, y - other.y, z - other.z)

    override fun times(scalar: Float): Vector3f =
        Vector3f(x * scalar, y * scalar, z * scalar)

    override fun div(scalar: Float): Vector3f =
        Vector3f(x / scalar, y / scalar, z / scalar)

    public fun floor(): Vector3f =
        Vector3f(kotlin.math.floor(x), kotlin.math.floor(y), kotlin.math.floor(z))

    public fun ceil(): Vector3f =
        Vector3f(kotlin.math.ceil(x), kotlin.math.ceil(y), kotlin.math.ceil(z))

    public fun toVector3i(): Vector3i =
        Vector3i(x.toInt(), y.toInt(), z.toInt())

    public fun toVector3l(): Vector3l =
        Vector3l(x.toLong(), y.toLong(), z.toLong())

    public fun toVector3d(): Vector3d =
        Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

    public fun toBlockPos(): Vector3i =
        floor().toVector3i()
}