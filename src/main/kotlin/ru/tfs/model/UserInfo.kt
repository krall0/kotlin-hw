package ru.tfs.model

import java.util.*

data class UserInfo(
    val id: UUID,
    val name: String,
    val docNumber: String,
    val inn: String
)
