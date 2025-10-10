package io.github.pandier.snowball.scheduler

public interface Task {
    public val canceled: Boolean

    public fun cancel()
}