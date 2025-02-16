package br.pucpr.authserver.vaccines.controller.requests

import br.pucpr.authserver.vaccines.Vaccine
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateVaccineRequest(
    @field:NotBlank
    var name: String?,
    @field:NotNull
    var brand: String?,
    @field:NotBlank
    var applicationDate: LocalDate?
) {
    fun toVaccine(): Vaccine = Vaccine(
        name = name!!,
        brand = brand!!,
        applicationDate = applicationDate!!
    )
}