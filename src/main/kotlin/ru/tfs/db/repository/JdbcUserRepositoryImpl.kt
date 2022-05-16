package ru.tfs.db.repository

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import ru.tfs.db.entity.UserEntity
import java.util.*

@Service
class JdbcUserRepositoryImpl(private val jdbcTemplate: JdbcTemplate) : UserRepository {

    companion object {
        private val rowMapper = RowMapper { rs, _ ->
            UserEntity().apply {
                id = rs.getObject("id", UUID::class.java)
                name = rs.getString("name")
                docNumber = rs.getString("doc_number")
                inn = rs.getString("inn")
            }
        }
    }

    private val selectByIdQuery = "select * from users where id = ?"
    private val selectAllByNameQuery = "select * from users where name = ? order by doc_number limit ? offset ?"
    private val insertQuery = "insert into users (id, name, doc_number, inn) values (?, ?, ?, ?)"

    override fun getById(userId: UUID): UserEntity? =
        try {
            jdbcTemplate.queryForObject(selectByIdQuery, rowMapper, userId)
        } catch (e: EmptyResultDataAccessException) {
            null
        }

    override fun getAllByName(name: String, page: Int, size: Int): List<UserEntity> =
        jdbcTemplate.query(selectAllByNameQuery, rowMapper, name, size, page * size)

    override fun add(userEntity: UserEntity) {
        PreparedStatementCreator { con ->
            con.prepareStatement(insertQuery).apply {
                setObject(1, userEntity.id)
                setString(2, userEntity.name)
                setString(3, userEntity.docNumber)
                setString(4, userEntity.inn)
            }
        }.also {
            jdbcTemplate.update(it)
        }
    }
}
