package ru.tfs.service

import org.springframework.stereotype.Component
import ru.tfs.model.Event

@Component
class SmsSender : NotificationSender {

    override fun sendNotification(event: Event) {
        println("Send sms event: $event")
    }
}
