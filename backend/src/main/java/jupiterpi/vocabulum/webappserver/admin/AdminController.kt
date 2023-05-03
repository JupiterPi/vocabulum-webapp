package jupiterpi.vocabulum.webappserver.admin

import jupiterpi.vocabulum.webappserver.auth.AuthController
import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider
import jupiterpi.vocabulum.webappserver.db.models.Voucher
import jupiterpi.vocabulum.webappserver.db.models.Vouchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/admin")
class AdminController {
    @Autowired
    lateinit var authController: AuthController

    private fun unauthorized(principal: Principal?): Boolean {
        return principal == null || !DbAuthenticationProvider.getUser(principal).isAdmin
    }

    @GetMapping("/pendingRegistrations")
    fun getPendingRegistrations(principal: Principal?): List<String>? {
        if (unauthorized(principal)) return null
        val registrations = authController.pendingRegistrations.registrations
        if (registrations.isEmpty()) return listOf("<No pending registrations>")
        return registrations.map { (id, registration) ->
            id + " " + (if (registration.isExpired) "expired" else "expiring") + " " + registration.expiration.toString()
        }
    }

    @GetMapping("/vouchers")
    fun getVouchers(principal: Principal?): List<String>? {
        if (unauthorized(principal)) return null
        val vouchers = Vouchers.find()
        if (vouchers.isEmpty()) return listOf("<No vouchers>")
        return vouchers.map {
            val expirationStr = (if (it.isExpired) "expired" else "expiring") + " " + it.expiration.toString()
            val usedStr = if (it.usedBy != null) "used by " + it.usedBy + " at " + it.usedAt else "not used"
            it.code + " ;; " + expirationStr + " ;; " + usedStr + " ;; " + it.note
        }
    }

    @PostMapping("/vouchers")
    fun createVouchers(principal: Principal?, @RequestBody dto: CreateVouchersDTO): List<String>? {
        if (unauthorized(principal)) return null
        val vouchers = generateSequence {
            Voucher(dto.expiration, dto.note).also { it.save() }
        }.take(dto.amount).toList()
        return vouchers.map { it.code }
    }
    data class CreateVouchersDTO(
        val expiration: Date,
        val amount: Int,
        val note: String,
    )
}