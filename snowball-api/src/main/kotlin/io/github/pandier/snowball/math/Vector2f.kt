package io.github.pandier.snowball.math

public data class Vector2f(
    public val x: Float,
    public val y: Float
) {
    public constructor(a: Float) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2f = Vector2f(0f, 0f)
    }

    public fun plus(x: Float, y: Float): Vector2f =
        Vector2f(this.x + x, this.y + y)

    public operator fun plus(other: Vector2f): Vector2f =
        plus(other.x, other.y)

    public fun minus(x: Float, y: Float): Vector2f =
        Vector2f(this.x - x, this.y - y)

    public operator fun minus(other: Vector2f): Vector2f =
        minus(other.x, other.y)

    public operator fun times(scalar: Float): Vector2f =
        Vector2f(x * scalar, y * scalar)

    public operator fun div(scalar: Float): Vector2f =
        Vector2f(x / scalar, y / scalar)
}
