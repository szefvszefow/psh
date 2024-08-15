package pl.hexlin.voting;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.javacord.api.entity.user.User;
import pl.hexlin.Instance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Suggestion {
    public final String id;
    public final String content;
    public final Instance instance;
    public int _for;
    public int against;
    public String isGoingToExpire;
    public String userId;
    public List<String> votedUsers;
    public boolean isFinished;

    public Suggestion(String id, String content, String isGoingToExpire, String userId, ArrayList<String> votedUsers, Boolean isFinished, Instance instance) {
        this.id = id;
        this.content = content;
        this.isGoingToExpire = isGoingToExpire;
        this.userId = userId;
        this.votedUsers = votedUsers;
        this.isFinished = isFinished;
        this.instance = instance;
    }

    public Suggestion(Document document, Instance instance) {
        id = document.getString("id");
        content = document.getString("content");
        _for = document.getInteger("for");
        against = document.getInteger("against");
        isGoingToExpire = document.getString("isgoingtoexpire");
        userId = document.getString("userId");
        votedUsers = document.getList("votedUsers", String.class);
        isFinished = document.getBoolean("isfinished");
        this.instance = instance;
    }

    public String getId() {
        return id;
    }

    public String getIsGoingToExpire() {
        return isGoingToExpire;
    }

    public void setIsGoingToExpire(String isGoingToExpire) {
        this.isGoingToExpire = isGoingToExpire;
    }

    public String getUserId() {
        return userId;
    }


    public boolean isExpired() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expireTime = LocalDateTime.parse(isGoingToExpire);
        return currentTime.isAfter(expireTime);
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished() {
        isFinished = true;
    }

    public void addFor(int count) {
        _for += count;
    }

    public void addAgainst(int count) {
        against += count;
    }

    public void addVotedUser(User user) {
        votedUsers.add(user.getIdAsString());
    }

    public void removeVotedUser(User user) {
        votedUsers.remove(user);
    }

    public String getVoteUserById(String targetUserId) {
        for (String user : votedUsers) {
            if (user.equals(targetUserId)) {
                return user;
            }
        }
        return null;
    }


    public void save() {
        Document document = new Document();
        document.append("id", id);
        document.append("content", content);
        document.append("for", _for);
        document.append("against", against);
        document.append("isgoingtoexpire", isGoingToExpire);
        document.append("userId", userId);
        document.append("isfinished", isFinished);
        document.append("votedUsers", votedUsers);
        instance.suggestionCollection.replaceOne(Filters.eq("id", id), document, new ReplaceOptions().upsert(true));
    }
}
