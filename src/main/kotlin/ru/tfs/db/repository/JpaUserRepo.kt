package ru.tfs.db.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import ru.tfs.db.entity.UserEntity
import java.util.*

interface JpaUserRepo : JpaRepository<UserEntity, UUID> {

    fun findAllByName(name: String, pageable: Pageable): List<UserEntity>
}
