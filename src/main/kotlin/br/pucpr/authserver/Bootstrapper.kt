package br.pucpr.authserver

import br.pucpr.authserver.roles.Role
import br.pucpr.authserver.roles.RoleRepository
import br.pucpr.authserver.users.User
import br.pucpr.authserver.users.UserRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class Bootstrapper(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository
): ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val adminRole = roleRepository.findByIdOrNull("ADMIN")
            ?: roleRepository.save(Role("ADMIN", "System Administrator"))
                .also { roleRepository.save(Role("USER", "Premium User")) }

        if (userRepository.findByRole("ADMIN").isEmpty()) {
            val admin = User(
                name = "Auth Server Administrator",
                email = "admin@authserver.com",
                password = "admin"
            )
            admin.roles.add(adminRole)
            userRepository.save(admin)
        }
    }
}