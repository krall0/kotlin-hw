package ru.tfs.jms

import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import ru.tfs.model.Event
import ru.tfs.model.EventStatus
import ru.tfs.model.EventType
import ru.tfs.service.EmailSender
import ru.tfs.service.EventService
import ru.tfs.service.PushSender
import ru.tfs.service.SmsSender

@Component
class EventConsumer(
    private val smsSender: SmsSender,
    private val pushSender: PushSender,
    private val emailSender: EmailSender,
    private val eventService: EventService
) {

    @JmsListener(destination = "event.queue")
    fun handleEvent(event: Event) {
        runCatching { sendNotification(event) }
            .onSuccess { eventService.updateEventStatus(event, EventStatus.DONE) }
            .onFailure { eventService.updateEventStatus(event, EventStatus.ERROR) }
    }

    private fun sendNotification(event: Event) {
        when (event.type) {
            EventType.SMS -> smsSender.sendNotification(event)
            EventType.PUSH -> pushSender.sendNotification(event)
            EventType.EMAIL -> emailSender.sendNotification(event)
        }
    }
}
