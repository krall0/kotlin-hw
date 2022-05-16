package ru.tfs.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ru.tfs.model.UserRequest
import ru.tfs.model.UserInfo
import ru.tfs.service.UserService
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addUser(@RequestBody userRequest: UserRequest) =
        userService.addUser(userRequest)

    @GetMapping("/{userId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserInfo(@PathVariable userId: UUID): UserInfo =
        userService.getUserInfo(userId)

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findUsersByName(
        @RequestParam("name") name: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): List<UserInfo> =
        userService.findUsers(name, page, size)
}
