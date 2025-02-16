package br.pucpr.authserver.vaccines

import br.pucpr.authserver.pets.Pet
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Entity
class Vaccine(
    @Id @GeneratedValue
    var id: Long? = null,
    @NotBlank
    var name: String = "",
    var brand: String = "",
    var applicationDate: LocalDate,

    @ManyToOne
    @JoinColumn(name = "idPet", nullable = false)
    var pet: Pet? = null
)