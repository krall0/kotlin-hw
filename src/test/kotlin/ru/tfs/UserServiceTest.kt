package ru.tfs

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.mockk.coEvery
import kotlinx.coroutines.delay
import org.awaitility.Awaitility.await
import org.awaitility.kotlin.untilNotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ru.tfs.db.repository.UserRepository
import ru.tfs.dto.UserDetails
import ru.tfs.dto.UserInfo
import ru.tfs.dto.UserRequest
import ru.tfs.dto.UserResponse
import ru.tfs.service.TaxClient
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest @Autowired constructor(private val mockMvc: MockMvc, private val objectMapper: ObjectMapper) {

    @MockkBean
    private lateinit var taxClient: TaxClient

    @SpykBean
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun initMock() {
        coEvery { taxClient.getUserDetails(any()) } coAnswers {
            delay(2000)
            UserDetails(
                name = "Olga",
                docNumber = "005678",
                inn = "TEST_INN"
            )
        }
    }

    @Test
    fun `add user and get by id`() {
        val userRequest = UserRequest(name = "Olga", docNumber = "005678")
        val userResponse = addUser(userRequest)

        waitForUserAdded(userResponse.userId)

        val expectedUserInfo = UserInfo(
            id = userResponse.userId,
            name = "Olga",
            docNumber = "005678",
            inn = "TEST_INN"
        )
        val actualUserInfo = getUser(userResponse.userId)
        assertEquals(actualUserInfo, expectedUserInfo)
    }

    private fun addUser(userRequest: UserRequest): UserResponse =
        mockMvc.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userRequest)
        }.readResponse()

    private fun getUser(userId: UUID): UserInfo =
        mockMvc.get("/users/{userId}", userId).readResponse()

    private inline fun <reified T> ResultActionsDsl.readResponse(expectedStatus: HttpStatus = HttpStatus.OK): T = this
        .andExpect { status { isEqualTo(expectedStatus.value()) } }
        .andReturn().response.getContentAsString(UTF_8)
        .let { if (T::class == String::class) it as T else objectMapper.readValue(it, T::class.java) }

    private fun waitForUserAdded(userId: UUID) {
        await() untilNotNull { userRepository.findByIdOrNull(userId) }
    }
}
