package br.pucpr.authserver.vaccines

import org.springframework.data.jpa.repository.JpaRepository

interface VaccineRepository: JpaRepository<Vaccine, Long> {
    fun findByPetId(petId: Long): List<Vaccine>
}