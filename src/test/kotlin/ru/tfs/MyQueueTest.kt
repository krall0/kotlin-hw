package ru.tfs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MyQueueTest {

    @Test
    fun `test for offer and poll methods`() {
        val myQueue = MyQueue<Int>()

        myQueue.offer(1)
        myQueue.offer(2)

        assertAll(
            { assertEquals(1, myQueue.poll()) },
            { assertEquals(2, myQueue.poll()) },
            { assertNull(myQueue.poll()) }
        )
    }

    @Test
    fun `test for peek method when queue is not empty`() {
        val myQueue = MyQueue<Int>()

        myQueue.offer(1)

        assertAll(
            { assertEquals(1, myQueue.peek()) },
            { assertEquals(1, myQueue.peek()) }
        )
    }

    @Test
    fun `test for peek method when queue is empty`() {
        val myQueue = MyQueue<Int>()

        assertNull(myQueue.peek())
    }

    @Test
    fun `test for element method when queue is not empty`() {
        val myQueue = MyQueue<Int>()

        myQueue.offer(1)

        assertAll(
            { assertEquals(1, myQueue.element()) },
            { assertEquals(1, myQueue.element()) }
        )
    }

    @Test
    fun `test for element method when queue is empty`() {
        val myQueue = MyQueue<Int>()

        assertThrows<NoSuchElementException> {
            myQueue.element()
        }
    }

    @Test
    fun `test for remove method`() {
        val myQueue = MyQueue<Int>()

        myQueue.offer(1)
        myQueue.offer(2)

        assertAll(
            { assertEquals(1, myQueue.remove()) },
            { assertEquals(2, myQueue.remove()) },
            {
                assertThrows<NoSuchElementException> {
                    myQueue.element()
                }
            }
        )
    }
}
