package ru.tfs

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ThreadPoolTest {

    private lateinit var task: Runnable

    @BeforeEach
    fun init() {
        task = mockk()
        every { task.run() } answers { println("Run task by ${Thread.currentThread().name}") }
    }

    @AfterEach
    fun clear() {
        clearAllMocks()
    }

    @Test
    fun `all tasks runs before shutdown`() {
        val threadPool = spyk(ThreadPool(5))
        repeat(5) {
            threadPool.execute(task)
        }

        threadPool.shutdown()

        verify(exactly = 5) { task.run() }
    }

    @Test
    fun `task not runs after shutdown`() {
        val threadPool = spyk(ThreadPool(5))
        repeat(5) {
            threadPool.execute(task)
        }
        threadPool.shutdown()

        Thread.sleep(2000)

        threadPool.execute(task)

        verify(exactly = 5) { task.run() }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 11])
    fun `invalid thread pool size`(poolSize: Int) {
        assertThrows(IllegalArgumentException::class.java) { ThreadPool(poolSize) }
    }
}
