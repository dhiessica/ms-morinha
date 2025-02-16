package br.pucpr.authserver.pets

import br.pucpr.authserver.users.User
import br.pucpr.authserver.vaccines.Vaccine
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Entity
class Pet(
    @Id @GeneratedValue
    var id: Long? = null,
    @NotBlank
    var name: String = "",
    var specie: String = "",
    var breed: String = "",
    var genre: String = "",
    var birthDate: LocalDate,
    var weight: Double = 0.0,

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    var user: User? = null,

    @OneToMany
    @JoinTable(
        name="PetVaccines",
        joinColumns = [JoinColumn(name = "idPet")],
        inverseJoinColumns = [JoinColumn(name = "idVaccine")]
    )
    val vaccines: MutableSet<Vaccine> = mutableSetOf()
)