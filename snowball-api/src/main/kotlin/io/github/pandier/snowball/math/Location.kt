package io.github.pandier.snowball.math

public data class Location(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
) {
    public constructor(x: Double, y: Double, z: Double) : this(x, y, z, 0f, 0f)
    public constructor(a: Double) : this(a, a, a)

    public companion object Constants {
        @JvmField
        public val ZERO: Location = Location(0.0, 0.0, 0.0, 0f, 0f)
    }

    val position: Vector3d get() = Vector3d(x, y, z)
    val rotation: Vector2f get() = Vector2f(yaw, pitch)

    public fun add(x: Double, y: Double, z: Double, yaw: Float, pitch: Float): Location =
        Location(this.x + x, this.y + y, this.z + z, this.yaw + yaw, this.pitch + pitch)

    public fun add(x: Double, y: Double, z: Double): Location =
        add(x, y, z, 0f, 0f)

    public fun add(other: Location): Location =
        add(other.x, other.y, other.z, other.yaw, other.pitch)

    public fun add(other: Vector3d): Location =
        add(other.x, other.y, other.z)

    public operator fun plus(other: Location): Location =
        add(other.x, other.y, other.z, other.yaw, other.pitch)

    public operator fun plus(other: Vector3d): Location =
        add(other.x, other.y, other.z)

    public fun sub(x: Double, y: Double, z: Double, yaw: Float, pitch: Float): Location =
        Location(this.x - x, this.y - y, this.z - z, this.yaw - yaw, this.pitch - pitch)

    public fun sub(x: Double, y: Double, z: Double): Location =
        sub(x, y, z, 0f, 0f)

    public fun sub(other: Location): Location =
        sub(other.x, other.y, other.z, other.yaw, other.pitch)

    public fun sub(other: Vector3d): Location =
        sub(other.x, other.y, other.z)

    public operator fun minus(other: Location): Location =
        sub(other.x, other.y, other.z, other.yaw, other.pitch)

    public operator fun minus(other: Vector3d): Location =
        sub(other.x, other.y, other.z)

    public fun mul(scalar: Double): Location =
        Location(x * scalar, y * scalar, z * scalar, yaw, pitch)

    public operator fun times(scalar: Double): Location =
        mul(scalar)

    public operator fun div(scalar: Double): Location =
        Location(x / scalar, y / scalar, z / scalar, yaw, pitch)

}
