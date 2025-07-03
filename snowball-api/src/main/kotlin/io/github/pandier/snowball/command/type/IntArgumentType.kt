package io.github.pandier.snowball.command.type

public class IntArgumentType(public val min: Int, public val max: Int) : ArgumentType<Int> {
    public constructor(): this(Int.MIN_VALUE, Int.MAX_VALUE)
    public constructor(min: Int): this(min, Int.MAX_VALUE)
}
