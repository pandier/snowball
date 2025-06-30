package io.github.pandier.snowball.command.type

class DoubleArgumentType(val min: Double, val max: Double) : ArgumentType<Double> {
    constructor(): this(Double.MIN_VALUE, Double.MAX_VALUE)
    constructor(min: Double): this(min, Double.MAX_VALUE)

    override val clazz: Class<Double> = Double::class.java
}
