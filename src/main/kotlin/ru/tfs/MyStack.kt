package ru.tfs

class MyStack<E> {

    private class Element<E>(
        val value: E,
        var next: Element<E>? = null
    )

    private var head: Element<E>? = null

    fun push(obj: E) {
        head = Element(obj, head)
    }

    fun pop(): E? {
        val value = peek()
        head = head?.next
        return value
    }

    fun peek(): E? {
        return head?.value
    }
}
