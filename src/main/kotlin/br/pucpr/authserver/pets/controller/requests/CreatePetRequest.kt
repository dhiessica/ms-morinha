package br.pucpr.authserver.pets.controller.requests

import br.pucpr.authserver.pets.Pet
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreatePetRequest(
    @field:NotBlank
    var name: String?,
    @field:NotBlank
    var specie: String?,
    @field:NotBlank
    var breed: String?,
    @field:NotBlank
    var genre: String?,
    @field:NotNull
    var birthDate: LocalDate?,
    @field:NotNull
    var weight: Double?
) {
    fun toPet(): Pet = Pet(
        name = name!!,
        specie = specie!!,
        breed = breed!!,
        genre = genre!!,
        birthDate = birthDate!!,
        weight = weight!!,
    )
}