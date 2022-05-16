package ru.tfs.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ru.tfs.dto.UserInfo
import ru.tfs.dto.UserRequest
import ru.tfs.dto.UserResponse
import ru.tfs.service.UserService
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addUser(@RequestBody userRequest: UserRequest): UserResponse =
        userService.addUser(userRequest)

    @GetMapping("/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserInfo(@PathVariable userId: UUID): UserInfo =
        userService.getUserInfo(userId)
}
