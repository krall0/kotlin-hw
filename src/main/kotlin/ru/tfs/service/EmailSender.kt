package ru.tfs.service

import org.springframework.stereotype.Component
import ru.tfs.model.Event

@Component
class EmailSender : NotificationSender {

    override fun sendNotification(event: Event) {
        println("Send email event: $event")
    }
}
