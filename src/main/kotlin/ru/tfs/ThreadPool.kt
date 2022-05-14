package ru.tfs

import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue

class ThreadPool(poolSize: Int) : Executor {

    private val maxPoolSize = 10

    private val threads = mutableListOf<WorkerThread>()

    private val taskQueue = LinkedBlockingQueue<Runnable>()

    init {
        require(poolSize in 1..maxPoolSize) { "Pool size must be in the range from 1 to $maxPoolSize" }

        repeat(poolSize) {
            val thread = WorkerThread(taskQueue)
            threads.add(thread)
            thread.start()
        }
    }

    override fun execute(task: Runnable) {
        synchronized(taskQueue) {
            taskQueue.add(task)
            (taskQueue as Object).notify()
        }
    }

    fun shutdown() {
        threads.forEach {
            it.interrupt()
        }
    }
}
