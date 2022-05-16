package ru.tfs.service

import org.springframework.stereotype.Service
import ru.tfs.db.entity.UserEntity
import ru.tfs.db.repository.UserRepository
import ru.tfs.exception.UserNotFoundException
import ru.tfs.model.UserRequest
import ru.tfs.model.UserInfo
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val taxClient: TaxClient
) {

    fun addUser(userRequest: UserRequest) {
        val inn = taxClient.getInn(userRequest.docNumber)
        requireNotNull(inn) { "Invalid doc number" }

        val userEntity = UserEntity().apply {
            this.id = UUID.randomUUID()
            this.name = userRequest.name
            this.docNumber = userRequest.docNumber
            this.inn = inn
        }
        userRepository.add(userEntity)
    }

    fun getUserInfo(userId: UUID): UserInfo {
        return userRepository.getById(userId)?.toUserInfo() ?: throw UserNotFoundException(userId)
    }

    fun findUsers(name: String, page: Int, size: Int): List<UserInfo> {
        return userRepository.getAllByName(name, page, size)
            .map { it.toUserInfo() }
    }

    private fun UserEntity.toUserInfo(): UserInfo {
        return UserInfo(
            id = this.id,
            name = this.name,
            docNumber = this.docNumber,
            inn = this.inn
        )
    }
}
