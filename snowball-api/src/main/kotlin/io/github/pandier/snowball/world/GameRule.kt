package io.github.pandier.snowball.world

public interface GameRule<T : Any> {
    public val valueClass: Class<T>

    public val defaultValue: T
}
