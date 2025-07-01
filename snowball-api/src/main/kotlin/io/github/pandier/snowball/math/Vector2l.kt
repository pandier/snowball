package io.github.pandier.snowball.math

public data class Vector2l(
    override val x: Long,
    override val y: Long
) : Vector2<Long> {
    public constructor(a: Long) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2l = Vector2l(0L, 0L)
    }

    public fun plus(x: Long, y: Long): Vector2l =
        Vector2l(this.x + x, this.y + y)

    override fun plus(other: Vector2<Long>): Vector2l =
        plus(other.x, other.y)

    public fun minus(x: Long, y: Long): Vector2l =
        Vector2l(this.x - x, this.y - y)

    override fun minus(other: Vector2<Long>): Vector2l =
        minus(other.x, other.y)

    override fun times(scalar: Long): Vector2l =
        Vector2l(x * scalar, y * scalar)

    override fun div(scalar: Long): Vector2l =
        Vector2l(x / scalar, y / scalar)
}
