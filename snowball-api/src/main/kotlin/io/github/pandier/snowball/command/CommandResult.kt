package io.github.pandier.snowball.command

public class CommandResult private constructor(public val value: Int) {
    public companion object Factory {
        @JvmStatic
        public fun of(value: Int): CommandResult = CommandResult(value)
        @JvmStatic
        public fun success(): CommandResult = CommandResult(0)
        @JvmStatic
        public fun failed(): CommandResult = CommandResult(1)
    }
}
