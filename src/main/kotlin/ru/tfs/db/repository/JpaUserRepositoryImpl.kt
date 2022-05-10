package ru.tfs.db.repository

import org.springframework.context.annotation.Primary
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.tfs.db.entity.UserEntity
import java.util.*

@Primary
@Service
class JpaUserRepositoryImpl(private val jpaUserRepo: JpaUserRepo) : UserRepository {

    override fun add(userEntity: UserEntity) {
        jpaUserRepo.save(userEntity)
    }

    override fun getById(userId: UUID): UserEntity? =
        jpaUserRepo.findByIdOrNull(userId)

    override fun getAllByName(name: String, page: Int, size: Int): List<UserEntity> =
        jpaUserRepo.findAllByName(
            name,
            PageRequest.of(page, size, Sort.sort(UserEntity::class.java).by(UserEntity::docNumber))
        )
}