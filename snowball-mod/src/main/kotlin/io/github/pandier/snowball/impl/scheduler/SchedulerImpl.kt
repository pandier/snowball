package io.github.pandier.snowball.impl.scheduler

import io.github.pandier.snowball.scheduler.Scheduler
import io.github.pandier.snowball.scheduler.Task
import org.apache.logging.log4j.LogManager
import java.util.function.Consumer

private val logger = LogManager.getLogger()

class SchedulerImpl : Scheduler {
    private val queue: MutableList<TaskImpl> = mutableListOf()
    private val tasks: MutableList<TaskImpl> = mutableListOf()
    private var tick: Long = 0

    @Synchronized
    override fun submit(delay: Long, interval: Long, executor: Consumer<Task>): Task {
        return TaskImpl(executor, interval, tick + delay.coerceAtLeast(0)).also {
            queue.add(it)
        }
    }

    @Synchronized
    private fun processQueue() {
        tick++
        tasks.addAll(queue)
        queue.clear()
    }

    /**
     * Should always be called from the main thread.
     */
    fun tick() {
        processQueue()
        tasks.removeIf(this::tickTask)
    }

    private fun tickTask(task: TaskImpl): Boolean {
        if (task.cancelled) {
            return true
        }

        // Execute if we've reached the next tick.
        // We're using unsigned comparison here so that the full range of the Long can be used.
        if (task.nextTick.toULong() <= tick.toULong()) {
            try {
                task.execute()
            } catch (ex: Exception) {
                // TODO: Log the plugin's name?
                logger.error("An error occurred thrown while executing task", ex)
            }

            // If the task is repeating and not cancelled, set the next tick.
            if (!task.cancelled && task.interval > 0) {
                task.nextTick = tick + task.interval
            } else {
                return true
            }
        }

        return false
    }
}