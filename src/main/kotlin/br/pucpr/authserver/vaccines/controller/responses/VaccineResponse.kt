package br.pucpr.authserver.vaccines.controller.responses

import br.pucpr.authserver.vaccines.Vaccine
import java.time.LocalDate

data class VaccineResponse(
    val id: Long,
    val name: String,
    var brand: String,
    var applicationDate: LocalDate,
) {
    constructor(vaccine: Vaccine): this(id = vaccine.id!!, vaccine.name, vaccine.brand, vaccine.applicationDate)
}