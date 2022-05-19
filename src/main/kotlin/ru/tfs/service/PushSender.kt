package ru.tfs.service

import org.springframework.stereotype.Component
import ru.tfs.model.Event

@Component
class PushSender : NotificationSender {

    override fun sendNotification(event: Event) {
        println("Send push event: $event")
    }
}
