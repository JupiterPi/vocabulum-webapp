package jupiterpi.vocabulum.webappserver.auth;

import jupiterpi.vocabulum.core.users.User;
import jupiterpi.vocabulum.core.util.Attachments;
import org.bson.Document;

public class WebappUser extends User {
    private boolean isProUser;

    public WebappUser(String username, String email, String password, boolean isProUser) {
        super(username, email, password);
        this.isProUser = isProUser;
    }

    public static WebappUser createUser(String username, String email, String password, boolean isProUser) {
        return new WebappUser(username, email, password, isProUser);
    }

    public WebappUser(Attachments attachments) {
        super(attachments);
        isProUser = attachments.consumeAttachment("webapp").getBoolean("isProUser");
    }

    public boolean isProUser() {
        return isProUser;
    }

    @Override
    protected Attachments generateAttachments() {
        Attachments attachments = super.generateAttachments();
        attachments.addAttachment("webapp", new Document("isProUser", isProUser));
        return attachments;
    }

    @Override
    public String toString() {
        return "WebappUser{" +
                "isProUser=" + isProUser +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}