package ru.tfs.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.tfs.db.entity.UserEntity
import ru.tfs.db.repository.UserRepository
import ru.tfs.exception.UserNotFoundException
import ru.tfs.dto.UserResponse
import ru.tfs.dto.UserDetails
import ru.tfs.dto.UserInfo
import ru.tfs.dto.UserRequest

import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val taxClient: TaxClient
) {

    fun addUser(userRequest: UserRequest): UserResponse {
        val userId = UUID.randomUUID()

        CoroutineScope(Dispatchers.Default).launch {
            val userDetails = taxClient.getUserDetails(userRequest)
            withContext(Dispatchers.IO) {
                userRepository.save(toUserEntity(userId, userDetails))
            }
        }

        return UserResponse(userId = userId)
    }

    fun getUserInfo(userId: UUID): UserInfo {
        return userRepository.findByIdOrNull(userId)?.toUserInfo() ?: throw UserNotFoundException(userId)
    }

    private fun UserEntity.toUserInfo(): UserInfo {
        return UserInfo(
            id = this.id,
            name = this.name,
            docNumber = this.docNumber,
            inn = this.inn
        )
    }

    private fun toUserEntity(userId: UUID, userDetails: UserDetails): UserEntity {
        return UserEntity().apply {
            this.id = userId
            this.name = userDetails.name
            this.docNumber = userDetails.docNumber
            this.inn = userDetails.inn
        }
    }
}
