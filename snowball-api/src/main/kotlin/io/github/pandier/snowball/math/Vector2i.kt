package io.github.pandier.snowball.math

public data class Vector2i(
    override val x: Int,
    override val y: Int
) : Vector2<Int> {
    public constructor(a: Int) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2i = Vector2i(0, 0)
    }

    public fun plus(x: Int, y: Int): Vector2i =
        Vector2i(this.x + x, this.y + y)

    override fun plus(other: Vector2<Int>): Vector2i =
        plus(other.x, other.y)

    public fun minus(x: Int, y: Int): Vector2i =
        Vector2i(this.x - x, this.y - y)

    override fun minus(other: Vector2<Int>): Vector2i =
        minus(other.x, other.y)

    override fun times(scalar: Int): Vector2i =
        Vector2i(x * scalar, y * scalar)

    override fun div(scalar: Int): Vector2i =
        Vector2i(x / scalar, y / scalar)
}
