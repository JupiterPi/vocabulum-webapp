package jupiterpi.vocabulum.webappserver.auth

import jupiterpi.vocabulum.webappserver.db.models.User
import jupiterpi.vocabulum.webappserver.db.models.Vouchers
import org.springframework.web.bind.annotation.*
import retrofit2.http.Path
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/auth")
class AuthController {
    @PostMapping("/register")
    fun register(@RequestHeader("Authorization") authHeader: String, @RequestBody dto: RegistrationDTO): UserDetailsDTO {
        val token = Auth.verifyAuthHeader(authHeader) ?: throw Exception("Invalid auth token")
        return UserDetailsDTO(User(token.uid, dto.username).also { it.save() })
    }
    data class RegistrationDTO(
        val username: String,
    )

    @GetMapping("/isAdmin")
    fun isAdmin(@RequestHeader("Authorization") authHeader: String) = IsAdminDTO(Auth.getToken(authHeader).claims["admin"] == true)
    data class IsAdminDTO(
        val isAdmin: Boolean,
    )

    @PostMapping("/makeAdmin/{uid}")
    fun makeAdmin(@RequestHeader("Authorization") authHeader: String, @PathVariable("uid") targetUid: String) {
        Auth.getToken(authHeader).assertAdmin()
        Auth.makeAdmin(targetUid, true)
    }
    @PostMapping("/revokeAdmin/{uid}")
    fun revokeAdmin(@RequestHeader("Authorization") authHeader: String, @PathVariable("uid") targetUid: String) {
        Auth.getToken(authHeader).assertAdmin()
        Auth.makeAdmin(targetUid, false)
    }

    @PostMapping("/useVoucher/{code}")
    fun useVoucher(@RequestHeader("Authorization") authHeader: String, @PathVariable code: String): UserDetailsDTO {
        val user = Auth.getUser(authHeader)
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

data class UserDetailsDTO(
    val username: String,
    val isProUser: Boolean,
    val discordUsername: String?,
) {
    constructor(user: User) : this(
        user.name,
        user.isProUser,
        user.discordUsername,
    )
}