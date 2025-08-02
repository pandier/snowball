package io.github.pandier.snowball.math

public data class Vector2i(
    public val x: Int,
    public val y: Int
) {
    public constructor(a: Int) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2i = Vector2i(0, 0)
    }

    public fun plus(x: Int, y: Int): Vector2i =
        Vector2i(this.x + x, this.y + y)

    public operator fun plus(other: Vector2i): Vector2i =
        plus(other.x, other.y)

    public fun minus(x: Int, y: Int): Vector2i =
        Vector2i(this.x - x, this.y - y)

    public operator fun minus(other: Vector2i): Vector2i =
        minus(other.x, other.y)

    public operator fun times(scalar: Int): Vector2i =
        Vector2i(x * scalar, y * scalar)

    public operator fun div(scalar: Int): Vector2i =
        Vector2i(x / scalar, y / scalar)
}
