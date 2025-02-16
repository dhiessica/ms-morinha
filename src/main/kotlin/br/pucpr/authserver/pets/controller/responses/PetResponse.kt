package br.pucpr.authserver.pets.controller.responses

import br.pucpr.authserver.pets.Pet
import java.time.LocalDate

data class PetResponse(
    val id: Long,
    val name: String,
    val specie: String,
    val breed: String,
    val genre: String,
    val birthDate: LocalDate,
    val weight: Double,
) {
    constructor(pet: Pet): this(id = pet.id!!, pet.name, pet.specie, pet.breed, pet.genre, pet.birthDate, pet.weight)
}
