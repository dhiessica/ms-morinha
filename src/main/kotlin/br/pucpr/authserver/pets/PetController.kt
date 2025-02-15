package br.pucpr.authserver.pets

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pets")
class PetController(
    val petService: PetService
) {

    @PostMapping("/user/{userId}")
    fun addPet(@PathVariable userId: Long, @RequestBody @Valid pet: CreatePetRequest) =
        petService.save(userId, pet.toPet())
            .let { PetResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping("/user/{userId}")
    fun findAllByUserId(@PathVariable userId: Long): ResponseEntity<List<PetResponse>> =
        petService.findAllPetsByUserIdOrNull(userId)
            ?.map { PetResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

}