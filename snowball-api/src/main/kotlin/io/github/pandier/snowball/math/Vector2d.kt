package io.github.pandier.snowball.math

public data class Vector2d(
    override val x: Double,
    override val y: Double
) : Vector2<Double> {
    public constructor(a: Double) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2d = Vector2d(0.0, 0.0)
    }

    public fun plus(x: Double, y: Double): Vector2d =
        Vector2d(this.x + x, this.y + y)

    override fun plus(other: Vector2<Double>): Vector2d =
        plus(other.x, other.y)

    public fun minus(x: Double, y: Double): Vector2d =
        Vector2d(this.x - x, this.y - y)

    override fun minus(other: Vector2<Double>): Vector2d =
        minus(other.x, other.y)

    override fun times(scalar: Double): Vector2d =
        Vector2d(x * scalar, y * scalar)

    override fun div(scalar: Double): Vector2d =
        Vector2d(x / scalar, y / scalar)
}
