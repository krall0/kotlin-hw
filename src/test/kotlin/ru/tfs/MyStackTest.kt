package ru.tfs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MyStackTest {

    @Test
    fun `test for push and pop methods`() {
        val myStack = MyStack<Int>()

        myStack.push(1)
        myStack.push(2)

        assertAll(
            { assertEquals(2, myStack.pop()) },
            { assertEquals(1, myStack.pop()) },
            { assertNull(myStack.pop()) }
        )
    }

    @Test
    fun `test for peek method when stack is not empty`() {
        val myStack = MyStack<Int>()

        myStack.push(1)
        myStack.push(2)

        assertAll(
            { assertEquals(2, myStack.peek()) },
            { assertEquals(2, myStack.peek()) }
        )
    }

    @Test
    fun `test for peek method when stack is empty`() {
        val myStack = MyStack<Int>()

        assertNull(myStack.peek())
    }
}