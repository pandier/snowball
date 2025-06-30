package io.github.pandier.snowball.command.type

public class DoubleArgumentType(public val min: Double, public val max: Double) : ArgumentType<Double> {
    public constructor(): this(Double.MIN_VALUE, Double.MAX_VALUE)
    public constructor(min: Double): this(min, Double.MAX_VALUE)

    override val clazz: Class<Double> = Double::class.java
}
