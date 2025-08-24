package io.github.pandier.snowball.impl.scheduler

import io.github.pandier.snowball.scheduler.Task
import java.util.function.Consumer

class TaskImpl(
    val executor: Consumer<Task>,
    val interval: Long,
    var nextTick: Long,
) : Task {
    @Volatile
    override var cancelled: Boolean = false
        private set

    fun execute() {
        executor.accept(this)
    }

    override fun cancel() {
        cancelled = true
    }
}