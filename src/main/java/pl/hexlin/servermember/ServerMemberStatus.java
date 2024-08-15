package pl.hexlin.servermember;

import org.bson.Document;

public class ServerMemberStatus {
    public String statusContent;
    public String imageURL;

    public ServerMemberStatus(Document document) {
        statusContent = document.getString("statusContent");
        imageURL = document.getString("imageURL");
    }

    public ServerMemberStatus(String statusContent, String imageURL) {
        this.statusContent = statusContent;
        this.imageURL = imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setStatusContent(String statusContent) {
        this.statusContent = statusContent;
    }

    public String getStatusContent() {
        return statusContent;
    }

    public String getImageURL() {
        return imageURL;
    }
}
