package br.pucpr.authserver.pets.controller

import br.pucpr.authserver.pets.Genre
import br.pucpr.authserver.pets.PetService
import br.pucpr.authserver.pets.controller.requests.CreatePetRequest
import br.pucpr.authserver.pets.controller.responses.PetResponse
import br.pucpr.authserver.users.SortDir
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
    fun findAllByUserId(@PathVariable userId: Long, @RequestParam genre: String? = null, @RequestParam dir: String = "ASC"): ResponseEntity<List<PetResponse>> {
        val genreInput = Genre.findOrNull(genre)
        val sortDir = SortDir.findOrNull(dir)
        return if (sortDir == null)
            ResponseEntity.badRequest().build()
        else
            return petService.findAllPetsByUserIdOrNull(userId, genreInput, sortDir)
            ?.map { PetResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    }

    @DeleteMapping("/id")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> = petService.delete(id)
        .let { ResponseEntity.ok().build() }
}