package io.github.pandier.snowball.math

public data class Vector2f(
    override val x: Float,
    override val y: Float
) : Vector2<Float> {
    public constructor(a: Float) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2f = Vector2f(0f, 0f)
    }

    override fun plus(other: Vector2<Float>): Vector2f =
        Vector2f(x + other.x, y + other.y)

    override fun minus(other: Vector2<Float>): Vector2f =
        Vector2f(x - other.x, y - other.y)

    override fun times(scalar: Float): Vector2f =
        Vector2f(x * scalar, y * scalar)

    override fun div(scalar: Float): Vector2f =
        Vector2f(x / scalar, y / scalar)
}
