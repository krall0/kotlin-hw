package ru.tfs

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate
import ru.tfs.db.entity.UserEntity
import ru.tfs.db.repository.JdbcUserRepositoryImpl
import java.util.*
import javax.sql.DataSource

@JdbcTest
class JdbcUserRepositoryTest(@Autowired private val ds: DataSource) {

    private val repository = JdbcUserRepositoryImpl(JdbcTemplate(ds))

    @Test
    fun `get user by id`() {
        val expected = UserEntity().apply {
            id = UUID.fromString("4e85ba66-f8af-41cd-a7d8-147ce35c03ec")
            name = "Ivan"
            docNumber = "1111567"
            inn = "001234567890"
        }

        val actual = repository.getById(UUID.fromString("4e85ba66-f8af-41cd-a7d8-147ce35c03ec"))

        assertEquals(actual, expected)
    }

    @Test
    fun `add user`() {
        val userId = UUID.randomUUID()
        val addedUserEntity = UserEntity().apply {
            id = userId
            name = "Olga"
            docNumber = "005678"
            inn = "111111111111"
        }
        repository.add(addedUserEntity)

        val actual = repository.getById(userId)

        assertEquals(actual, addedUserEntity)
    }

    @Test
    fun `get users by name with pagination`() {
        val expectedUsers = listOf(
            UserEntity().apply {
                id = UUID.fromString("7034e6da-93e3-49fc-ab8a-791a876e6797")
                name = "Andrey"
                docNumber = "2564099"
                inn = "001233458799"
            },
            UserEntity().apply {
                id = UUID.fromString("4e4502f8-dd78-4c91-bed9-86c13873d109")
                name = "Andrey"
                docNumber = "2564100"
                inn = "001233458800"
            }
        )

        val actualUsers = repository.getAllByName(name = "Andrey", page = 1, size = 2)

        assertEquals(actualUsers, expectedUsers)
    }
}
