package br.pucpr.authserver.pets

import java.time.LocalDate

data class PetResponse(
    val id: Long,
    val name: String,
    var birthDate: LocalDate,
    var breed: String,
    var weight: Double,
) {
    constructor(pet: Pet): this(id = pet.id!!, pet.name, pet.birthDate, pet.breed, pet.weight)
}
