package ru.tfs.repository

import org.springframework.stereotype.Component
import ru.tfs.model.UserInfo
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class UserRepository {

    private val users = ConcurrentHashMap<UUID, UserInfo>()

    fun add(user: UserInfo): UserInfo {
        users[user.id] = user
        return user
    }

    fun getById(userId: UUID): UserInfo? {
        return users[userId]
    }

    fun getAllByName(name: String, page: Int, size: Int): List<UserInfo> {
        return users.values.asSequence()
            .filter { name == it.name }
            .sortedBy { it.docNumber }
            .drop(page * size)
            .take(size)
            .toList()
    }
}
