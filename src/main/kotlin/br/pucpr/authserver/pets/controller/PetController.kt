package br.pucpr.authserver.pets.controller

import br.pucpr.authserver.pets.PetService
import br.pucpr.authserver.pets.controller.requests.CreatePetRequest
import br.pucpr.authserver.pets.controller.responses.PetResponse
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
    fun insert(@PathVariable userId: Long, @RequestBody @Valid pet: CreatePetRequest) =
        petService.save(userId, pet.toPet())
            .let { PetResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping("/user/{userId}")
    fun findAllByUserId(@PathVariable userId: Long): ResponseEntity<List<PetResponse>> =
        petService.findAllPetsByUserIdOrNull(userId)
            ?.map { PetResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/id")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> = petService.delete(id)
        .let { ResponseEntity.ok().build() }
}