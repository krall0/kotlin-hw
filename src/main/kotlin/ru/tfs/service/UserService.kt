package ru.tfs.service

import org.springframework.stereotype.Service
import ru.tfs.exception.UserNotFoundException
import ru.tfs.model.UserRequest
import ru.tfs.model.UserInfo
import ru.tfs.repository.UserRepository
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val taxClient: TaxClient
) {

    fun addUser(userRequest: UserRequest): UserInfo {
        val inn = taxClient.getInn(userRequest.docNumber)
        requireNotNull(inn) { "Invalid doc number" }

        val userInfo = UserInfo(
            id = UUID.randomUUID(),
            name = userRequest.name,
            docNumber = userRequest.docNumber,
            inn = inn
        )
        return userRepository.add(userInfo)
    }

    fun getUserInfo(userId: UUID): UserInfo {
        return userRepository.getById(userId) ?: throw UserNotFoundException(userId)
    }

    fun findUsers(name: String, page: Int, size: Int): List<UserInfo> {
        return userRepository.getAllByName(name, page, size)
    }
}
