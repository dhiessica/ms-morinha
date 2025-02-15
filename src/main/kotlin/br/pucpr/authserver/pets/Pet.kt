package br.pucpr.authserver.pets

import br.pucpr.authserver.users.User
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

@Entity
class Pet(
    @Id @GeneratedValue
    var id: Long? = null,
    @NotBlank
    var name: String = "",
    var birthDate: LocalDate,
    var breed: String = "",
    var weight: Double = 0.0,

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    var user: User? = null
)