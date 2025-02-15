package br.pucpr.authserver.users.controller.requests

import br.pucpr.authserver.users.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CreateUserRequest(
    @field:NotBlank
    var name: String?,
    @field:Email
    @field:NotBlank
    var email: String?,
    @field:NotBlank
    var password: String?,
) {
    fun toUser(): User = User(
        name = name!!,
        email = email!!,
        password = password!!
    )
}