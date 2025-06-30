package io.github.pandier.snowball.command.type

class FloatArgumentType(val min: Float, val max: Float) : ArgumentType<Float> {
    constructor(): this(Float.MIN_VALUE, Float.MAX_VALUE)
    constructor(min: Float): this(min, Float.MAX_VALUE)

    override val clazz: Class<Float> = Float::class.java
}
