package io.github.pandier.snowball.command.type

class LongArgumentType(val min: Long, val max: Long) : ArgumentType<Long> {
    constructor(): this(Long.MIN_VALUE, Long.MAX_VALUE)
    constructor(min: Long): this(min, Long.MAX_VALUE)

    override val clazz: Class<Long> = Long::class.java
}
