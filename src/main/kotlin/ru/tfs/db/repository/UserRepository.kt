package ru.tfs.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.tfs.db.entity.UserEntity
import java.util.*

interface UserRepository : JpaRepository<UserEntity, UUID>
