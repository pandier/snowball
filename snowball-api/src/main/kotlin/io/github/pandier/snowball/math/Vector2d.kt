package io.github.pandier.snowball.math

public data class Vector2d(
    public val x: Double,
    public val y: Double
) {
    public constructor(a: Double) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2d = Vector2d(0.0, 0.0)
    }

    public fun plus(x: Double, y: Double): Vector2d =
        Vector2d(this.x + x, this.y + y)

    public operator fun plus(other: Vector2d): Vector2d =
        plus(other.x, other.y)

    public fun minus(x: Double, y: Double): Vector2d =
        Vector2d(this.x - x, this.y - y)

    public operator fun minus(other: Vector2d): Vector2d =
        minus(other.x, other.y)

    public operator fun times(scalar: Double): Vector2d =
        Vector2d(x * scalar, y * scalar)

    public operator fun div(scalar: Double): Vector2d =
        Vector2d(x / scalar, y / scalar)
}
