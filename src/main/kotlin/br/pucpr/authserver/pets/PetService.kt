package br.pucpr.authserver.pets

import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.users.UserService
import org.springframework.stereotype.Service

@Service
class PetService(
    val petRepository: PetRepository,
    val userService: UserService
) {
    fun save(userId: Long, pet: Pet): Pet {
        val user = userService.findByIdOrNull(userId) ?: throw NotFoundException("User $userId not found: ")
        pet.user = user
        return petRepository.save(pet)
    }

    fun findAllPetsByUserIdOrNull(userId: Long): List<Pet>? {
        userService.findByIdOrNull(userId) ?: throw NotFoundException("User $userId not found: ")
        return petRepository.findByUserId(userId)
    }
}