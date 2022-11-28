package jupiterpi.vocabulum.webappserver.auth;

public class CredentialsVerificationDTO {
    private boolean valid;

    public CredentialsVerificationDTO(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }
}
