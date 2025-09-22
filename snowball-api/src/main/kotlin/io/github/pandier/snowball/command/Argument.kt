package io.github.pandier.snowball.command

import io.github.pandier.snowball.command.type.ArgumentType

public class Argument<T>(
    public val name: String,
    public val type: ArgumentType<T>,
    public val optional: Boolean = false,
)
