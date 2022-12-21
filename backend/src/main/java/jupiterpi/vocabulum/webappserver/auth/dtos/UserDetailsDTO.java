package jupiterpi.vocabulum.webappserver.auth.dtos;

import jupiterpi.vocabulum.core.users.User;

public class UserDetailsDTO {
    private String username;
    private String email;
    private boolean isProUser;
    private String discordUsername;
    private boolean isAdmin;

    public static UserDetailsDTO fromUser(User user) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.username = user.getName();
        dto.email = user.getEmail();
        dto.isProUser = user.isProUser();
        dto.discordUsername = user.getDiscordUsername();
        dto.isAdmin = user.isAdmin();
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

    public String getDiscordUsername() {
        return discordUsername;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }
}