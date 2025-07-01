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

    public fun plus(x: Float, y: Float): Vector2f =
        Vector2f(this.x + x, this.y + y)

    override fun plus(other: Vector2<Float>): Vector2f =
        plus(other.x, other.y)

    public fun minus(x: Float, y: Float): Vector2f =
        Vector2f(this.x - x, this.y - y)

    override fun minus(other: Vector2<Float>): Vector2f =
        minus(other.x, other.y)

    override fun times(scalar: Float): Vector2f =
        Vector2f(x * scalar, y * scalar)

    override fun div(scalar: Float): Vector2f =
        Vector2f(x / scalar, y / scalar)
}
