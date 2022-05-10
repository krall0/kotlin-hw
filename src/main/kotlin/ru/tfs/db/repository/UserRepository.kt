package ru.tfs.db.repository

import ru.tfs.db.entity.UserEntity
import java.util.*

interface UserRepository {

    fun add(userEntity: UserEntity)

    fun getById(userId: UUID): UserEntity?

    fun getAllByName(name: String, page: Int, size: Int): List<UserEntity>
}
