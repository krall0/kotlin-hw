package ru.tfs.model

import java.io.Serializable

data class Event(
    val id: Int,
    val type: EventType,
    val body: String
): Serializable
