package ru.tfs.exception

import java.util.*

class UserNotFoundException(userId: UUID) : RuntimeException("User with id=$userId not found")
