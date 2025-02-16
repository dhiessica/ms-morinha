package br.pucpr.authserver.vaccines.controller

import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.vaccines.VaccineService
import br.pucpr.authserver.vaccines.controller.requests.CreateVaccineRequest
import br.pucpr.authserver.vaccines.controller.responses.VaccineResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/vaccines")
class VaccinesController(
    val vaccineService: VaccineService
) {
    @PostMapping("/pet/{petId}")
    fun insert(@PathVariable petId: Long, @RequestBody @Valid vaccine: CreateVaccineRequest) =
        vaccineService.save(petId, vaccine.toVaccine())
            .let { VaccineResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping("/pet/{petId}")
    fun findAllByPetId(@PathVariable petId: Long, @RequestParam dir: String = "ASC"): ResponseEntity<List<VaccineResponse>> {
        val sortDir = SortDir.findOrNull(dir)
        return if (sortDir == null)
            ResponseEntity.badRequest().build()
        else vaccineService.findAllVaccinesByPetIdOrNull(petId, sortDir)
            ?.map { VaccineResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/id")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> = vaccineService.delete(id)
        .let { ResponseEntity.ok().build() }
}