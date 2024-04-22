package id.co.anfal.bsn.config;

import id.co.anfal.bsn.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Long> {

    /**
     * AuditAware<Long> interface ini mengambil tipe data generic <Long> yang menunjukkan jenis identitas auditor (dalam hal ini, menggunakan tipe Long untuk ID pengguna).
     */

    /**
     * Implementasi AuditorAware digunakan dalam Spring Data JPA untuk entitas yang memiliki kolom pencatatan audit seperti
     * createdBy, createdDate, lastModifiedBy, lastModifiedDate. Dengan menggunakan AuditorAware,
     * Anda dapat melacak siapa yang membuat atau mengubah entitas ini,
     * yang berguna untuk audit dan pemantauan aplikasi.
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        // object authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}
