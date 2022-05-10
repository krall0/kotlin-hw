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
        val statementCreator = PreparedStatementCreator { con ->
            val ps = con.prepareStatement(insertQuery)
            ps.setObject(1, userEntity.id)
            ps.setString(2, userEntity.name)
            ps.setString(3, userEntity.docNumber)
            ps.setString(4, userEntity.inn)
            ps
        }
        jdbcTemplate.update(statementCreator)
    }
}
