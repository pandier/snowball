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

    override fun plus(other: Vector2<Double>): Vector2d =
        Vector2d(x + other.x, y + other.y)

    override fun minus(other: Vector2<Double>): Vector2d =
        Vector2d(x - other.x, y - other.y)

    override fun times(scalar: Double): Vector2d =
        Vector2d(x * scalar, y * scalar)

    override fun div(scalar: Double): Vector2d =
        Vector2d(x / scalar, y / scalar)
}
