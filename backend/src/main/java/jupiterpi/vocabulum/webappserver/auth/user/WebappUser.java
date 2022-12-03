package jupiterpi.vocabulum.webappserver.auth.user;

import jupiterpi.vocabulum.core.users.User;
import jupiterpi.vocabulum.core.util.Attachments;
import org.bson.Document;

public class WebappUser extends User {
    private boolean isProUser;
    private String discordUsername;

    public WebappUser(String username, String email, String password, boolean isProUser, String discordUsername) {
        super(username, email, password);
        this.isProUser = isProUser;
        this.discordUsername = discordUsername;
    }

    public static WebappUser createUser(String username, String email, String password, boolean isProUser, String discordUsername) {
        return new WebappUser(username, email, password, isProUser, discordUsername);
    }

    public WebappUser(Attachments attachments) {
        super(attachments);
        isProUser = attachments.consumeAttachment("webapp").getBoolean("isProUser");
        discordUsername = attachments.consumeAttachment("discordbot").getString("username");
    }

    @Override
    protected Attachments generateAttachments() {
        Attachments attachments = super.generateAttachments();
        attachments.addAttachment("webapp", new Document("isProUser", isProUser));
        attachments.addAttachment("discordbot", new Document("username", discordUsername));
        return attachments;
    }

    /* getters, setters */

    public boolean isProUser() {
        return isProUser;
    }

    public WebappUser setProUser(boolean proUser) {
        isProUser = proUser;
        return this;
    }

    public String getDiscordUsername() {
        return discordUsername;
    }

    public WebappUser setDiscordUsername(String discordUsername) {
        this.discordUsername = discordUsername;
        return this;
    }

    /* to string */

    @Override
    public String toString() {
        return "WebappUser{" +
                "isProUser=" + isProUser +
                ", discordUsername='" + discordUsername + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}