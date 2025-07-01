package io.github.pandier.snowball.math

public interface Vector2<T> {
    public val x: T
    public val y: T

    public operator fun plus(other: Vector2<T>): Vector2<T>
    public operator fun minus(other: Vector2<T>): Vector2<T>
    public operator fun times(scalar: T): Vector2<T>
    public operator fun div(scalar: T): Vector2<T>
}