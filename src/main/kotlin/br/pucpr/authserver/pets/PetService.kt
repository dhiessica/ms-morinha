package br.pucpr.authserver.pets

import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.UserService
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
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

    fun findByIdOrNull(id: Long) = petRepository.findByIdOrNull(id)


    fun findAllPetsByUserIdOrNull(userId: Long, genre: Genre?, dir: SortDir): List<Pet>? {
        userService.findByIdOrNull(userId) ?: throw NotFoundException("User $userId not found: ")
        var pets = petRepository.findByUserId(userId)
        if (genre != null) pets = pets.filter { it.genre.uppercase() == genre.name }
        return when (dir) {
            SortDir.ASC -> pets.sortedBy { it.name }
            SortDir.DESC -> pets.sortedByDescending { it.name }
        }
    }

    fun delete(id: Long) = petRepository.deleteById(id)
}