package io.github.pandier.snowball.scheduler

public interface Task {
    public val cancelled: Boolean

    public fun cancel()
}