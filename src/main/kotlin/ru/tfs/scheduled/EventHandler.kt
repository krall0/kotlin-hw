package ru.tfs.scheduled

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.tfs.service.EventService

@Component
class EventHandler(private val eventService: EventService) {

    @Scheduled(cron = "0 0 * * * *")
    fun handleEvents() {
        eventService.getNewEvents()
            .forEach { eventService.processEvent(it) }
    }
}
