package io.github.pandier.snowball.command

import io.github.pandier.snowball.command.type.ArgumentType

class Argument<T>(val type: ArgumentType<T>, var name: String)
