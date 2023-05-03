package jupiterpi.vocabulum.webappserver.auth

import jupiterpi.vocabulum.webappserver.db.models.User
import jupiterpi.vocabulum.webappserver.db.models.Users
import jupiterpi.vocabulum.webappserver.db.models.Vouchers
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
        val userFound = Users.findByCredentials(dto.username, dto.password)
        return if (userFound == null) {
            CredentialsVerificationDTO(false, "")
        } else {
            CredentialsVerificationDTO(true, userFound.email)
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
        val voucher = Vouchers.findByCode(code)
        if (voucher != null && !voucher.isExpired && !voucher.isUsed) {
            if (!user.isProUser) {
                voucher.useNow(user.completeKey)
                voucher.save()

                user.proExpiration = voucher.expiration
                user.save()
            }
        }
        return UserDetailsDTO(user)
    }
}

data class RegistrationDTO(
    val username: String,
    val email: String,
    val password: String,
)

data class UserDetailsDTO(
    val username: String,
    val email: String,
    val isProUser: Boolean,
    val discordUsername: String?,
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