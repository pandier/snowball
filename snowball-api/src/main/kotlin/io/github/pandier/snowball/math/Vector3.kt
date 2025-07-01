package io.github.pandier.snowball.math

public interface Vector3<T> {
    public val x: T
    public val y: T
    public val z: T

    public operator fun plus(other: Vector3<T>): Vector3<T>
    public operator fun minus(other: Vector3<T>): Vector3<T>
    public operator fun times(scalar: T): Vector3<T>
    public operator fun div(scalar: T): Vector3<T>
}