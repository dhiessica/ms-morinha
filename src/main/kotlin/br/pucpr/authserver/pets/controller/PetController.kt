package br.pucpr.authserver.pets.controller

import br.pucpr.authserver.pets.Genre
import br.pucpr.authserver.pets.PetService
import br.pucpr.authserver.pets.controller.requests.CreatePetRequest
import br.pucpr.authserver.pets.controller.responses.PetResponse
import br.pucpr.authserver.security.UserToken
import br.pucpr.authserver.users.SortDir
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pets")
class PetController(
    val petService: PetService
) {

    @PostMapping("/user/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun insert(@PathVariable id: String, @RequestBody @Valid pet: CreatePetRequest, auth: Authentication): ResponseEntity<PetResponse> {
        val user = auth.principal as UserToken
        val uid = if (id == "me") user.id else id.toLong()

        return if (user.id == uid || user.isAdmin) {
            petService.save(uid, pet.toPet())
                .let { PetResponse(it) }
                .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @GetMapping("/user/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun findAllByUserId(@PathVariable id: String, @RequestParam genre: String? = null, @RequestParam dir: String = "ASC", auth: Authentication): ResponseEntity<List<PetResponse>> {
        val genreInput = Genre.findOrNull(genre)
        val sortDir = SortDir.findOrNull(dir)

        val user = auth.principal as UserToken
        val uid = if (id == "me") user.id else id.toLong()

        return if (user.id == uid || user.isAdmin) {
            return if (sortDir == null)
                ResponseEntity.badRequest().build()
            else
                return petService.findAllPetsByUserIdOrNull(uid, genreInput, sortDir)
                    ?.map { PetResponse(it) }
                    ?.let { ResponseEntity.ok(it) }
                    ?: ResponseEntity.notFound().build()
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun delete(@PathVariable id: Long, auth: Authentication): ResponseEntity<Void> {
        val user = auth.principal as UserToken
        val pets = findAllByUserId(id = user.id.toString(), auth = auth).body
        val petToDelete = pets?.find { it.id == id }

        return if (pets != null && petToDelete != null) {
            petService.delete(id)
                .let { ResponseEntity.ok().build() }
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

    }
}