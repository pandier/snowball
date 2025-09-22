package io.github.pandier.snowball.impl.scheduler

import io.github.pandier.snowball.scheduler.Task
import java.util.function.Consumer
import kotlin.test.*

class SchedulerImplTest {

    class TaskExecutor : Consumer<Task> {
        private var executed = false

        fun checkExecuted(): Boolean {
            return executed.also {
                executed = false
            }
        }

        override fun accept(t: Task) {
            executed = true
        }
    }

    @Test
    fun executesTaskNextTick() {
        val scheduler = SchedulerImpl()
        val executor = TaskExecutor()
        scheduler.submit(executor)

        scheduler.tick()
        assertTrue(executor.checkExecuted())
    }

    @Test
    fun executesTaskWithTwoTickDelay() {
        val scheduler = SchedulerImpl()
        val executor = TaskExecutor()
        scheduler.submit(2, executor)

        scheduler.tick()
        assertFalse(executor.checkExecuted(), "Task should not be executed yet")

        scheduler.tick()
        assertTrue(executor.checkExecuted(), "Task should be executed after two ticks")
    }

    @Test
    fun executesTaskWithHundredTickDelay() {
        val scheduler = SchedulerImpl()
        val executor = TaskExecutor()
        scheduler.submit(100, executor)

        repeat(99) {
            scheduler.tick()
            assertFalse(executor.checkExecuted(), "Task should not be executed yet (tick ${it + 1})")
        }

        scheduler.tick()
        assertTrue(executor.checkExecuted(), "Task should be executed after two ticks")
    }

    @Test
    fun executesTaskOnce() {
        val scheduler = SchedulerImpl()
        val executor = TaskExecutor()
        scheduler.submit(executor)

        scheduler.tick()
        assertTrue(executor.checkExecuted(), "Task should be executed first tick")

        repeat(10) {
            scheduler.tick()
            assertFalse(executor.checkExecuted(), "Task should never be executed after first tick")
        }
    }

    @Test
    fun executesTaskRepeatedly() {
        val scheduler = SchedulerImpl()
        val executor = TaskExecutor()
        scheduler.submit(0, 1, executor)

        repeat(10) {
            scheduler.tick()
            assertTrue(executor.checkExecuted(), "Task should be executed repeatedly (tick ${it + 1})")
        }
    }

    @Test
    fun executesTaskRepeatedlyWithInterval() {
        val scheduler = SchedulerImpl()
        val executor = TaskExecutor()
        scheduler.submit(0, 2, executor)

        repeat(10) {
            scheduler.tick()
            assertTrue(executor.checkExecuted(), "Task should be executed at tick ${it * 2 + 1}")

            scheduler.tick()
            assertFalse(executor.checkExecuted(), "Task should not be executed at tick ${it * 2 + 2}")
        }
    }

    @Test
    fun executesTaskRepeatedlyWithTwoTickDelay() {
        val scheduler = SchedulerImpl()
        val executor = TaskExecutor()
        scheduler.submit(2, 1, executor)

        scheduler.tick()
        assertFalse(executor.checkExecuted(), "Task should not be executed yet")

        repeat(10) {
            scheduler.tick()
            assertTrue(executor.checkExecuted(), "Task should be executed repeatedly (tick ${it + 1} after delay)")
        }
    }

    @Test
    fun stopsExecutingCancelledRepeatingTask() {
        val scheduler = SchedulerImpl()
        val executor = TaskExecutor()
        val task = scheduler.submit(0, 1, executor)

        scheduler.tick()
        assertTrue(executor.checkExecuted(), "Task should be executed first tick")

        task.cancel()

        repeat(10) {
            scheduler.tick()
            assertFalse(executor.checkExecuted(), "Task should never be executed after cancellation (tick ${it + 1} after cancellation)")
        }
    }

    @Test
    fun stopsExecutingCancelledDelayedTask() {
        val scheduler = SchedulerImpl()
        val executor = TaskExecutor()
        val task = scheduler.submit(2, executor)

        scheduler.tick()
        assertFalse(executor.checkExecuted(), "Task should not be executed first tick")

        task.cancel()

        repeat(10) {
            scheduler.tick()
            assertFalse(executor.checkExecuted(), "Task should never be executed after cancellation (tick ${it + 1} after cancellation)")
        }
    }
}