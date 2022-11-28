package jupiterpi.vocabulum.webappserver.auth;

public class UserDetailsDTO {
    private String username;
    private String email;
    private boolean isProUser;

    public static UserDetailsDTO fromUser(WebappUser user) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.username = user.getName();
        dto.email = user.getEmail();
        dto.isProUser = user.isProUser();
        return dto;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean getIsProUser() {
        return isProUser;
    }
}