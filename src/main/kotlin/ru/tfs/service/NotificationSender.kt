package ru.tfs.service

import ru.tfs.model.Event

interface NotificationSender {

    fun sendNotification(event: Event)
}
