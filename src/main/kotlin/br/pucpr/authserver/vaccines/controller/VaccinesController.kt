package br.pucpr.authserver.vaccines.controller

import br.pucpr.authserver.pets.controller.PetController
import br.pucpr.authserver.security.UserToken
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.vaccines.VaccineService
import br.pucpr.authserver.vaccines.controller.requests.CreateVaccineRequest
import br.pucpr.authserver.vaccines.controller.responses.VaccineResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vaccines")
class VaccinesController(
    val vaccineService: VaccineService,
    val petController: PetController
) {
    @PostMapping("/pet/{petId}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun insert(@PathVariable petId: Long, @RequestBody @Valid vaccine: CreateVaccineRequest, auth: Authentication): ResponseEntity<VaccineResponse> {
        val user = auth.principal as UserToken
        val pets = petController.findAllByUserId(id = user.id.toString(), auth = auth).body
        val currentPet = pets?.find { it.id == petId }

        return if (pets != null && currentPet != null) {
            vaccineService.save(petId, vaccine.toVaccine())
                .let { VaccineResponse(it) }
                .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @GetMapping("/pet/{petId}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun findAllByPetId(@PathVariable petId: Long, @RequestParam dir: String = "ASC", auth: Authentication): ResponseEntity<List<VaccineResponse>> {
        val user = auth.principal as UserToken
        val pets = petController.findAllByUserId(id = user.id.toString(), auth = auth).body
        val currentPet = pets?.find { it.id == petId }

        return if (pets != null && currentPet != null) {
            val sortDir = SortDir.findOrNull(dir)
            return if (sortDir == null)
                ResponseEntity.badRequest().build()
            else vaccineService.findAllVaccinesByPetIdOrNull(petId, sortDir)
                ?.map { VaccineResponse(it) }
                ?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.notFound().build()
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @DeleteMapping("/pet/{petId}/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("permitAll()")
    fun delete(@PathVariable petId: Long,@PathVariable id: Long, auth: Authentication): ResponseEntity<Void> {
        val user = auth.principal as UserToken
        val pets = petController.findAllByUserId(id = user.id.toString(), auth = auth).body
        val currentPet = pets?.find { it.id == petId }
        val vaccines = if (currentPet != null ) findAllByPetId(petId = currentPet.id, auth = auth).body else null
        val vaccineToDelete = vaccines?.find { it.id == id }

        return if (pets != null && currentPet != null && vaccines != null && vaccineToDelete != null) {
            vaccineService.delete(id)
                .let { ResponseEntity.ok().build() }
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }
}