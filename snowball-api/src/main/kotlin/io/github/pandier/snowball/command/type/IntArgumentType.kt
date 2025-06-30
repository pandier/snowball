package io.github.pandier.snowball.command.type

class IntArgumentType(val min: Int, val max: Int) : ArgumentType<Int> {
    constructor(): this(Int.MIN_VALUE, Int.MAX_VALUE)
    constructor(min: Int): this(min, Int.MAX_VALUE)

    override val clazz: Class<Int> = Int::class.java
}
