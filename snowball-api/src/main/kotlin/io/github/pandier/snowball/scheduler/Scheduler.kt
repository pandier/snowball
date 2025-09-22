package io.github.pandier.snowball.scheduler

import java.util.function.Consumer

public interface Scheduler {
    public fun submit(executor: Consumer<Task>): Task =
        submit(0, 0, executor)

    public fun submit(delay: Long, executor: Consumer<Task>): Task =
        submit(delay, 0, executor)

    public fun submit(delay: Long, interval: Long, executor: Consumer<Task>): Task
}