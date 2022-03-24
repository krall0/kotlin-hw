package ru.tfs

class MyQueue<E> {

    private class Element<E>(
        val value: E,
        var next: Element<E>? = null
    )

    private var head: Element<E>? = null
    private var tail: Element<E>? = null

    fun element(): E {
        return peek() ?: throw NoSuchElementException()
    }

    fun remove(): E {
        return poll() ?: throw NoSuchElementException()
    }

    fun peek(): E? {
        return head?.value
    }

    fun poll(): E? {
        val value = peek()
        head = head?.next
        if (head == null) {
            tail = null
        }
        return value
    }

    fun offer(obj: E): Boolean {
        val newElement = Element(obj)
        if (head == null) {
            tail = newElement
            head = tail
        } else {
            tail!!.next = newElement
            tail = tail!!.next
        }
        return true
    }
}