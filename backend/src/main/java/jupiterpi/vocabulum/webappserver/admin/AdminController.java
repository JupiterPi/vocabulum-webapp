package jupiterpi.vocabulum.webappserver.admin;

import jupiterpi.vocabulum.core.db.Database;
import jupiterpi.vocabulum.webappserver.auth.AuthController;
import jupiterpi.vocabulum.webappserver.auth.DbAuthenticationProvider;
import jupiterpi.vocabulum.webappserver.auth.registration.Registration;
import jupiterpi.vocabulum.webappserver.db.WebappDatabase;
import jupiterpi.vocabulum.webappserver.db.vouchers.Voucher;
import jupiterpi.vocabulum.webappserver.db.vouchers.Vouchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AuthController authController;

    private boolean unauthorized(Principal principal) {
        return principal == null || !DbAuthenticationProvider.getUser(principal).isAdmin();
    }

    @GetMapping("/pendingRegistrations")
    public List<String> getPendingRegistrations(Principal principal) {
        if (unauthorized(principal)) return null;

        Map<String, Registration> registrations = authController.pendingRegistrations.getRegistrations();
        if (registrations.size() == 0) return List.of("<No pending registrations>");
        List<String> registrationsStr = new ArrayList<>();
        registrations.forEach((id, registration) -> {
            registrationsStr.add(id + " " + (registration.isExpired() ? "expired" : "expiring") + " " + registration.getExpiration().toString());
        });
        return registrationsStr;
    }

    @PostMapping("/reloadUsers")
    public void reloadUsers(Principal principal) {
        if (unauthorized(principal)) return;

        Database.get().getUsers().load();
    }

    @PostMapping("/reloadHistories")
    public void reloadHistories(Principal principal) {
        if (unauthorized(principal)) return;

        WebappDatabase.get().getHistories().load();
    }

    @GetMapping("/vouchers")
    public List<String> getVouchers(Principal principal) {
        if (unauthorized(principal)) return null;

        List<String> vouchersStr = new ArrayList<>();
        List<Voucher> vouchers = WebappDatabase.get().getVouchers().getAll();
        if (vouchers.size() == 0) return List.of("<No vouchers>");
        for (Voucher voucher : vouchers) {
            boolean expired = voucher.getExpiration().getTime() <= new Date().getTime();
            String expirationStr = (expired ? "expired" : "expiring") + " " + voucher.getExpiration().toString();

            boolean used = !voucher.getUsedBy().isEmpty();
            String usedStr = (used ? "used by " + voucher.getUsedBy() + " at " + voucher.getUsedAt() : "not used");

            vouchersStr.add(voucher.getCode() + " ;; " + expirationStr + " ;; " + usedStr + " ;; " + voucher.getNote());
        }
        return vouchersStr;
    }

    @PostMapping("/vouchers")
    public List<String> createVouchers(Principal principal, @RequestBody CreateVouchersDTO dto) {
        if (unauthorized(principal)) return null;

        if (dto.getExpiration() == null) return null;
        return WebappDatabase.get().getVouchers().createVouchers(dto.getExpiration(), dto.getAmount(), dto.getNote()).stream()
                .map(voucher -> voucher.getCode())
                .collect(Collectors.toList());
    }
    static class CreateVouchersDTO {
        private Date expiration;
        private int amount;
        private String note;

        public CreateVouchersDTO() {}
        public CreateVouchersDTO(Date expiration, int amount, String note) {
            this.expiration = expiration;
            this.amount = amount;
            this.note = note;
        }

        public Date getExpiration() {
            return expiration;
        }

        public int getAmount() {
            return amount;
        }

        public String getNote() {
            return note;
        }

        @Override
        public String toString() {
            return "CreateVouchersDTO{" +
                    "expiration=" + expiration +
                    ", amount=" + amount +
                    ", note='" + note + '\'' +
                    '}';
        }
    }

    @PostMapping("/reloadVouchers")
    public void reloadVouchers(Principal principal) {
        if (unauthorized(principal)) return;

        WebappDatabase.get().getVouchers().load();
    }

}