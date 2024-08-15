package pl.hexlin.question;


import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.javacord.api.entity.user.User;
import pl.hexlin.Instance;

import java.util.ArrayList;
import java.util.List;

public class Question {
    public final String id;
    public final String content;
    public final Instance instance;
    public int _for;
    public int against;
    public String isGoingToExpire;
    public String userId;
    public List<String> votedUsers;
    public boolean isFinished;

    public Question(String id, String content, String userId, ArrayList<String> votedUsers, Instance instance) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.votedUsers = votedUsers;
        this.instance = instance;
    }

    public Question(Document document, Instance instance) {
        id = document.getString("id");
        content = document.getString("content");
        _for = document.getInteger("for");
        against = document.getInteger("against");
        userId = document.getString("userId");
        votedUsers = document.getList("votedUsers", String.class);
        this.instance = instance;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
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
        document.append("userId", userId);
        document.append("votedUsers", votedUsers);
        instance.questionCollection.replaceOne(Filters.eq("id", id), document, new ReplaceOptions().upsert(true));
    }
}
