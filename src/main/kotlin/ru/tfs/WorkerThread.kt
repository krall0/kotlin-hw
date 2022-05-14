package ru.tfs

import java.util.concurrent.LinkedBlockingQueue

class WorkerThread(private val taskQueue: LinkedBlockingQueue<Runnable>) : Thread() {

    override fun run() {
        while (true) {
            if (currentThread().isInterrupted) {
                break
            }
            var task: Runnable? = null
            synchronized(taskQueue) {
                if (taskQueue.isEmpty()) {
                    try {
                        (taskQueue as Object).wait()
                    } catch (e: InterruptedException) {
                        currentThread().interrupt()
                    }
                }
                if (!currentThread().isInterrupted) {
                    task = taskQueue.poll()
                }
            }
            task?.run()
        }
    }
}
