package br.pucpr.authserver.vaccines

import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.pets.PetService
import org.springframework.stereotype.Service

@Service
class VaccineService(
    val vaccineRepository: VaccineRepository,
    val petService: PetService
) {
    fun save(petId: Long, vaccine: Vaccine): Vaccine {
        val pet = petService.findByIdOrNull(petId) ?: throw NotFoundException("Pet $petId not found: ")
        vaccine.pet = pet
        return vaccineRepository.save(vaccine)
    }

    fun findAllVaccinesByPetIdOrNull(petId: Long): List<Vaccine>? {
        petService.findByIdOrNull(petId) ?: throw NotFoundException("Pet $petId not found: ")
        return vaccineRepository.findByPetId(petId)
    }

    fun delete(id: Long) = vaccineRepository.deleteById(id)
}