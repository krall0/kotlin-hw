package ru.tfs

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.verify
import org.awaitility.Awaitility.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo
import org.awaitility.kotlin.withAlias
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import ru.tfs.db.entity.EventEntity
import ru.tfs.db.repository.EventRepository
import ru.tfs.model.EventStatus
import ru.tfs.model.EventType
import ru.tfs.scheduled.EventHandler
import ru.tfs.service.EmailSender
import ru.tfs.service.PushSender
import ru.tfs.service.SmsSender

@SpringBootTest
@AutoConfigureMockMvc
class EventHandlerTest {

    @SpykBean
    private lateinit var eventHandler: EventHandler

    @SpykBean
    private lateinit var eventRepository: EventRepository

    @MockkBean
    private lateinit var smsSender: SmsSender

    @MockkBean
    private lateinit var pushSender: PushSender

    @MockkBean
    private lateinit var emailSender: EmailSender

    @BeforeEach
    fun initRepository() {
        eventRepository.save(EventEntity().apply {
            type = EventType.SMS
            body = "Test body sms"
            status = EventStatus.NEW
        })
        eventRepository.save(EventEntity().apply {
            type = EventType.PUSH
            body = "Test body push"
            status = EventStatus.NEW
        })
        eventRepository.save(EventEntity().apply {
            type = EventType.EMAIL
            body = "Test body email"
            status = EventStatus.NEW
        })
    }

    @AfterEach
    fun cleanAll() {
        clearAllMocks()
        eventRepository.deleteAll()
    }

    @Test
    fun `process all new events successfully`() {
        every { smsSender.sendNotification(any()) } returns Unit
        every { pushSender.sendNotification(any()) } returns Unit
        every { emailSender.sendNotification(any()) } returns Unit

        eventHandler.handleEvents()

        waitForAllEventsStatusChangedTo(EventStatus.DONE)

        verify(exactly = 1) { smsSender.sendNotification(any()) }
        verify(exactly = 1) { pushSender.sendNotification(any()) }
        verify(exactly = 1) { emailSender.sendNotification(any()) }
    }

    @Test
    fun `process all new events with error`() {
        every { smsSender.sendNotification(any()) } throws RuntimeException()
        every { pushSender.sendNotification(any()) } throws RuntimeException()
        every { emailSender.sendNotification(any()) } throws RuntimeException()

        eventHandler.handleEvents()

        waitForAllEventsStatusChangedTo(EventStatus.ERROR)

        verify(exactly = 1) { smsSender.sendNotification(any()) }
        verify(exactly = 1) { pushSender.sendNotification(any()) }
        verify(exactly = 1) { emailSender.sendNotification(any()) }
    }

    private fun waitForAllEventsStatusChangedTo(status: EventStatus) {
        await() withAlias ("The status of event should change to DONE") untilCallTo {
            eventRepository.findAll()
        } matches {
            it!!.all { eventEntity -> eventEntity.status == status }
        }
    }
}
