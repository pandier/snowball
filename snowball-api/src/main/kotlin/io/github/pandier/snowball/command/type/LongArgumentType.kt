package io.github.pandier.snowball.command.type

public class LongArgumentType(public val min: Long, public val max: Long) : ArgumentType<Long> {
    public constructor(): this(Long.MIN_VALUE, Long.MAX_VALUE)
    public constructor(min: Long): this(min, Long.MAX_VALUE)
}
