package jupiterpi.vocabulum.webappserver.auth

import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.users.User
import jupiterpi.vocabulum.webappserver.db.WebappDatabase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/auth")
class AuthController {
    var pendingRegistrations = PendingRegistrations()

    @PostMapping("/register")
    fun register(@RequestBody dto: RegistrationDTO) = pendingRegistrations.addPendingRegistration(dto)

    @PostMapping("/confirmRegistration/{id}")
    fun confirmRegistration(@PathVariable id: String): String {
        return pendingRegistrations.confirmRegistration(id)?.name ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Pending registration not found")
    }

    @PostMapping("/verifyCredentials")
    fun verifyCredentials(@RequestBody dto: CredentialsDTO): CredentialsVerificationDTO {
        val usersFound = Database.get().users.all.filter { it.email == dto.username && it.password == dto.password }
        return if (usersFound.size != 1) {
            CredentialsVerificationDTO(false, "")
        } else {
            CredentialsVerificationDTO(true, usersFound[0].email)
        }
    }
    data class CredentialsDTO(
        val username: String,
        val password: String,
    )
    data class CredentialsVerificationDTO(
        val valid: Boolean,
        val email: String,
    )

    @PostMapping("/login")
    fun login(principal: Principal): UserDetailsDTO = UserDetailsDTO(DbAuthenticationProvider.getUser(principal))

    @PostMapping("/useVoucher/{code}")
    fun useVoucher(principal: Principal, @PathVariable code: String): UserDetailsDTO {
        val user = DbAuthenticationProvider.getUser(principal)
        val voucher = WebappDatabase.Vouchers.getVoucher(code)
        if (voucher != null && !voucher.isExpired && !voucher.isUsed) {
            if (!user.isProUser) {
                voucher.useAndSave(user.email, Date())
                user.proExpiration = voucher.expiration
                user.saveEntity()
            }
        }
        return UserDetailsDTO(user)
    }
}

data class RegistrationDTO(
    val username: String?,
    val email: String?,
    val password: String?,
)

data class UserDetailsDTO(
    val username: String,
    val email: String,
    val isProUser: Boolean,
    val discordUsername: String,
    val isAdmin: Boolean,
) {
    constructor(user: User) : this(
        user.name,
        user.email,
        user.isProUser,
        user.discordUsername,
        user.isAdmin,
    )
}