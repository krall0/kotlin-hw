package ru.tfs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.SpykBean
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.core.test.TestCase
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ru.tfs.model.AddUserRequest
import ru.tfs.model.UserInfo
import ru.tfs.repository.UserRepository
import ru.tfs.service.TaxClient
import java.util.*
import kotlin.text.Charsets.UTF_8

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest(private val mockMvc: MockMvc, private val objectMapper: ObjectMapper) : FeatureSpec() {

    @MockkBean
    private lateinit var taxClient: TaxClient

    @SpykBean
    private lateinit var userRepository: UserRepository

    override fun extensions(): List<Extension> = listOf(SpringExtension)

    override fun beforeEach(testCase: TestCase) {
        initUserRepository()

        every { taxClient.getInn(any()) } returns "TEST_INN"
        every { taxClient.getInn("INVALID_DOCNUM") } returns null
    }

    init {
        feature("add user") {
            scenario("success") {
                val addUserRequest = AddUserRequest(name = "Olga", docNumber = "005678")
                val userInfo = addUser(addUserRequest)
                userInfo should {
                    it.name shouldBe "Olga"
                    it.docNumber shouldBe "005678"
                    it.inn shouldBe "TEST_INN"
                }
            }
            scenario("failure - invalid doc number") {
                val addUserRequest = AddUserRequest(name = "Olga", docNumber = "INVALID_DOCNUM")
                addUserAndGetStatus(addUserRequest) shouldBe HttpStatus.BAD_REQUEST.value()
            }
        }

        feature("get user by id") {
            scenario("success") {
                val userInfo = getUser(UUID.fromString("4e85ba66-f8af-41cd-a7d8-147ce35c03ec"))
                userInfo should {
                    it.id shouldBe UUID.fromString("4e85ba66-f8af-41cd-a7d8-147ce35c03ec")
                    it.name shouldBe "Ivan"
                    it.docNumber shouldBe "1111567"
                    it.inn shouldBe "001234567890"
                }
            }
            scenario("failure - not found user") {
                getUserAndGetStatus(UUID.randomUUID()) shouldBe HttpStatus.NOT_FOUND.value()
            }
        }

        feature("find users by name") {
            scenario("success") {
                val users = findUsersByName(name = "Andrey", page = 1, size = 2)
                users shouldBe listOf(
                    UserInfo(
                        id = UUID.fromString("7034e6da-93e3-49fc-ab8a-791a876e6797"),
                        name = "Andrey",
                        docNumber = "2564099",
                        inn = "001233458799"
                    ),
                    UserInfo(
                        id = UUID.fromString("4e4502f8-dd78-4c91-bed9-86c13873d109"),
                        name = "Andrey",
                        docNumber = "2564100",
                        inn = "001233458800"
                    )
                )
            }
            scenario("success with default pagination") {
                val users = findUsersByName(name = "Andrey")
                users shouldBe listOf(
                    UserInfo(
                        id = UUID.fromString("50184951-d37d-413a-94bf-492be15f3663"),
                        name = "Andrey",
                        docNumber = "2564097",
                        inn = "001234566754"
                    ),
                    UserInfo(
                        id = UUID.fromString("943686a9-f3ba-402a-8a55-5d7468597758"),
                        name = "Andrey",
                        docNumber = "2564098",
                        inn = "001233458798"
                    ),
                    UserInfo(
                        id = UUID.fromString("7034e6da-93e3-49fc-ab8a-791a876e6797"),
                        name = "Andrey",
                        docNumber = "2564099",
                        inn = "001233458799"
                    ),
                    UserInfo(
                        id = UUID.fromString("4e4502f8-dd78-4c91-bed9-86c13873d109"),
                        name = "Andrey",
                        docNumber = "2564100",
                        inn = "001233458800"
                    )
                )
            }
        }
    }

    private fun addUser(addUserRequest: AddUserRequest): UserInfo =
        mockMvc.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(addUserRequest)
        }.readResponse()

    private fun addUserAndGetStatus(addUserRequest: AddUserRequest) =
        mockMvc.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(addUserRequest)
        }
            .andReturn().response.status

    private fun getUser(userId: UUID): UserInfo =
        mockMvc.get("/users/{userId}", userId).readResponse()

    private fun getUserAndGetStatus(userId: UUID) =
        mockMvc.get("/users/{userId}", userId).andReturn().response.status

    private fun findUsersByName(name: String, page: Int? = null, size: Int? = null): List<UserInfo> =
        mockMvc.get("/users?name={name}&page={page}&size={size}", name, page, size).readResponse()

    private inline fun <reified T> ResultActionsDsl.readResponse(expectedStatus: HttpStatus = HttpStatus.OK): T = this
        .andExpect { status { isEqualTo(expectedStatus.value()) } }
        .andReturn().response.getContentAsString(UTF_8)
        .let { if (T::class == String::class) it as T else objectMapper.readValue(it) }

    private val users = listOf(
        UserInfo(
            id = UUID.fromString("4e85ba66-f8af-41cd-a7d8-147ce35c03ec"),
            name = "Ivan",
            docNumber = "1111567",
            inn = "001234567890"
        ),
        UserInfo(
            id = UUID.fromString("50184951-d37d-413a-94bf-492be15f3663"),
            name = "Andrey",
            docNumber = "2564097",
            inn = "001234566754"
        ),
        UserInfo(
            id = UUID.fromString("943686a9-f3ba-402a-8a55-5d7468597758"),
            name = "Andrey",
            docNumber = "2564098",
            inn = "001233458798"
        ),
        UserInfo(
            id = UUID.fromString("7034e6da-93e3-49fc-ab8a-791a876e6797"),
            name = "Andrey",
            docNumber = "2564099",
            inn = "001233458799"
        ),
        UserInfo(
            id = UUID.fromString("4e4502f8-dd78-4c91-bed9-86c13873d109"),
            name = "Andrey",
            docNumber = "2564100",
            inn = "001233458800"
        )

    )

    private fun initUserRepository() {
        users.forEach(userRepository::add)
    }
}
