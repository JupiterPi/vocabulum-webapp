package jupiterpi.vocabulum.webappserver.auth.registration;

import java.util.Date;

public class Registration {
    private RegistrationDTO dto;
    private Date expiration;

    public Registration(RegistrationDTO dto, Date expiration) {
        this.dto = dto;
        this.expiration = expiration;
    }

    public RegistrationDTO getDto() {
        return dto;
    }

    public boolean isExpired() {
        return expiration.getTime() <= new Date().getTime();
    }
}