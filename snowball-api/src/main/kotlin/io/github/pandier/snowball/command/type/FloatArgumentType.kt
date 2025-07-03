package io.github.pandier.snowball.command.type

public class FloatArgumentType(public val min: Float, public val max: Float) : ArgumentType<Float> {
    public constructor(): this(Float.MIN_VALUE, Float.MAX_VALUE)
    public constructor(min: Float): this(min, Float.MAX_VALUE)
}
