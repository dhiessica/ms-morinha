package br.pucpr.authserver.users.controller

import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.UserService
import br.pucpr.authserver.users.controller.requests.CreateUserRequest
import br.pucpr.authserver.users.controller.requests.LoginRequest
import br.pucpr.authserver.users.controller.responses.LoginResponse
import br.pucpr.authserver.users.controller.responses.UserResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
) {
    @PostMapping
    fun insert(@RequestBody @Valid user: CreateUserRequest) =
        userService.save(user.toUser())
            .let { UserResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun findAll(@RequestParam dir: String = "ASC", @RequestParam role: String? = null): ResponseEntity<List<UserResponse>> {
        val sortDir = SortDir.findOrNull(dir)
        return if (sortDir == null)
            ResponseEntity.badRequest().build()
        else
            userService.findAll(sortDir, role)
                .map { UserResponse(it) }
                .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) =
        userService.findByIdOrNull(id)
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> = userService.delete(id)
        .let { ResponseEntity.ok().build() }

    @PutMapping("/{id}/roles/{role}")
    fun grant(@PathVariable id: Long, @PathVariable role: String): ResponseEntity<Void> =
        if (userService.addRole(id,role))
            ResponseEntity.ok().build()
        else
            ResponseEntity.noContent().build()

    @PostMapping("/login")
    fun login(@Valid @RequestBody user: LoginRequest): ResponseEntity<LoginResponse> =
        userService.login(user.email!!, user.password!!)
            ?.let { ResponseEntity.ok().body(it) }
            ?: ResponseEntity.notFound().build()


}