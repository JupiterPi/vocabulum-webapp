package jupiterpi.vocabulum.webappserver.admin

import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.webappserver.auth.AuthController
import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider
import jupiterpi.vocabulum.webappserver.db.WebappDatabase
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

    @PostMapping("/reloadUsers")
    fun reloadUsers(principal: Principal?) {
        if (unauthorized(principal)) return
        Database.get().users.load()
    }

    @PostMapping("/reloadHistories")
    fun reloadHistories(principal: Principal?) {
        if (unauthorized(principal)) return
        WebappDatabase.Histories.load()
    }

    @GetMapping("/vouchers")
    fun getVouchers(principal: Principal?): List<String>? {
        if (unauthorized(principal)) return null
        val vouchers = WebappDatabase.Vouchers.getAll()
        if (vouchers.isEmpty()) return listOf("<No vouchers>")
        return vouchers.map {
            val expirationStr = (if (it.isExpired) "expired" else "expiring") + " " + it.expiration.toString()
            val usedStr = if (it.usedBy.isNotEmpty()) "used by " + it.usedBy + " at " + it.usedAt else "not used"
            it.code + " ;; " + expirationStr + " ;; " + usedStr + " ;; " + it.note
        }
    }

    @PostMapping("/vouchers")
    fun createVouchers(principal: Principal?, @RequestBody dto: CreateVouchersDTO): List<String>? {
        if (unauthorized(principal)) return null
        return WebappDatabase.Vouchers.createVouchers(dto.expiration, dto.amount, dto.note).map { it.code }
    }
    data class CreateVouchersDTO(
        val expiration: Date,
        val amount: Int,
        val note: String,
    )

    @PostMapping("/reloadVouchers")
    fun reloadVouchers(principal: Principal?) {
        if (unauthorized(principal)) return
        WebappDatabase.Vouchers.load()
    }
}