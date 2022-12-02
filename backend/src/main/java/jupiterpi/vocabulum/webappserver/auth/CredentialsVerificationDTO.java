package jupiterpi.vocabulum.webappserver.auth;

public class CredentialsVerificationDTO {
    private boolean valid;
    private String email;

    public CredentialsVerificationDTO(boolean valid, String email) {
        this.valid = valid;
        this.email = email;
    }

    public boolean isValid() {
        return valid;
    }

    public String getEmail() {
        return email;
    }
}
