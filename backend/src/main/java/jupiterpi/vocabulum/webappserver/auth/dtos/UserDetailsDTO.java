package jupiterpi.vocabulum.webappserver.auth.dtos;

import jupiterpi.vocabulum.webappserver.auth.user.WebappUser;

public class UserDetailsDTO {
    private String username;
    private String email;
    private boolean isProUser;
    private String discordUsername;

    public static UserDetailsDTO fromUser(WebappUser user) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.username = user.getName();
        dto.email = user.getEmail();
        dto.isProUser = user.isProUser();
        dto.discordUsername = user.getDiscordUsername();
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

    @Override
    public String toString() {
        return "UserDetailsDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isProUser=" + isProUser +
                ", discordUsername='" + discordUsername + '\'' +
                '}';
    }
}