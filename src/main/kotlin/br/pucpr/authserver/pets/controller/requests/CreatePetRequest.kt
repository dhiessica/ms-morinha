package br.pucpr.authserver.pets.controller.requests

import br.pucpr.authserver.pets.Pet
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreatePetRequest(
    @field:NotBlank
    var name: String?,
    @field:NotNull
    var birthDate: LocalDate?,
    @field:NotBlank
    var breed: String?,
    @field:NotNull
    var weight: Double?
) {
    fun toPet(): Pet = Pet(
        name = name!!,
        birthDate = birthDate!!,
        breed = breed!!,
        weight = weight!!,
    )
}