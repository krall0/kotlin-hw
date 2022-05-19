package ru.tfs.service

import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tfs.db.entity.EventEntity
import ru.tfs.db.repository.EventRepository
import ru.tfs.model.Event
import ru.tfs.model.EventStatus

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val jmsTemplate: JmsTemplate
) {

    fun getNewEvents(): List<Event> =
        eventRepository.findAllByStatus(EventStatus.NEW)
            .map { it.toEvent() }

    @Transactional
    fun processEvent(event: Event) {
        updateEventStatus(event, EventStatus.IN_PROCESS)
        jmsTemplate.convertAndSend("event.queue", event)
    }

    @Transactional
    fun updateEventStatus(event: Event, status: EventStatus) =
        eventRepository.updateStatus(event.id, status)

    private fun EventEntity.toEvent(): Event =
        Event(
            id = this.id!!,
            type = this.type,
            body = this.body
        )
}
