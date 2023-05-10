package jupiterpi.vocabulum.webappserver.admin

import jupiterpi.vocabulum.webappserver.auth.Auth
import jupiterpi.vocabulum.webappserver.auth.AuthController
import jupiterpi.vocabulum.webappserver.db.models.Voucher
import jupiterpi.vocabulum.webappserver.db.models.Vouchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("/admin")
class AdminController {
    private fun checkAuth(authHeaders: String) {
        if (Auth.getToken(authHeaders).claims["admin"] != true) throw Exception("Admin-only!")
    }

    @GetMapping("/vouchers")
    fun getVouchers(@RequestHeader("Authorization") authHeaders: String): List<String>? {
        checkAuth(authHeaders)
        val vouchers = Vouchers.find()
        if (vouchers.isEmpty()) return listOf("<No vouchers>")
        return vouchers.map {
            val expirationStr = (if (it.isExpired) "expired" else "expiring") + " " + it.expiration.toString()
            val usedStr = if (it.usedBy != null) "used by " + it.usedBy + " at " + it.usedAt else "not used"
            it.code + " ;; " + expirationStr + " ;; " + usedStr + " ;; " + it.note
        }
    }

    @PostMapping("/vouchers")
    fun createVouchers(@RequestHeader("Authorization") authHeaders: String, @RequestBody dto: CreateVouchersDTO): List<String>? {
        checkAuth(authHeaders)
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