package br.pucpr.authserver.pets

import org.springframework.data.jpa.repository.JpaRepository

interface PetRepository: JpaRepository<Pet, Long> {
    fun findByUserId(userId: Long): List<Pet>
}