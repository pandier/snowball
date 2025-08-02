package io.github.pandier.snowball.math

public data class Vector2l(
    public val x: Long,
    public val y: Long
) {
    public constructor(a: Long) : this(a, a)

    public companion object Constants {
        @JvmStatic
        public val ZERO: Vector2l = Vector2l(0L, 0L)
    }

    public fun plus(x: Long, y: Long): Vector2l =
        Vector2l(this.x + x, this.y + y)

    public operator fun plus(other: Vector2l): Vector2l =
        plus(other.x, other.y)

    public fun minus(x: Long, y: Long): Vector2l =
        Vector2l(this.x - x, this.y - y)

    public operator fun minus(other: Vector2l): Vector2l =
        minus(other.x, other.y)

    public operator fun times(scalar: Long): Vector2l =
        Vector2l(x * scalar, y * scalar)

    public operator fun div(scalar: Long): Vector2l =
        Vector2l(x / scalar, y / scalar)
}
