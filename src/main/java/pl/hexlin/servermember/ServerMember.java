package pl.hexlin.servermember;


import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import pl.hexlin.Instance;
import pl.hexlin.finance.Investment;

import java.util.ArrayList;
import java.util.List;

public class ServerMember {
    private final Long userId;
    private final Instance instance;
    public Boolean isDumaMember;
    private Integer messagesSent;
    public Integer roubles;
    public List<ServerMemberStatus> serverMemberStatus;
    public List<Investment> investments;
    public String lastActivityDate;

    public ServerMember(Long userId, Integer messagesSent, Instance instance, Boolean isDumaMember, Integer roubles, String lastActivityDate) {
        this.userId = userId;
        this.messagesSent = messagesSent;
        this.instance = instance;
        this.isDumaMember = isDumaMember;
        this.roubles = roubles;
        this.lastActivityDate = lastActivityDate;
    }

    public ServerMember(Document document, Instance instance) {
        userId = document.getLong("userId");
        messagesSent = document.getInteger("messagesSent");
        isDumaMember = document.getBoolean("isDumaMember");
        roubles = document.getInteger("roubles");
        lastActivityDate = document.getString("lastActivityDate");

        if (lastActivityDate == null) {
            lastActivityDate = "Nieznana data";
        }

        List<Document> statusDocument = document.getList("serverMemberStatus", Document.class);
        if (statusDocument != null) {
            serverMemberStatus = new ArrayList<>();
            for (Document candidateDoc : statusDocument) {
                ServerMemberStatus serverMemberStatus1 = new ServerMemberStatus(candidateDoc);
                serverMemberStatus.add(serverMemberStatus1);
            }
        } else {
            serverMemberStatus = new ArrayList<>();
        }

        List<Document> investmentDocument = document.getList("investments", Document.class);
        if (investmentDocument != null) {
            investments = new ArrayList<>();
            for (Document candidateDoc : investmentDocument) {
                Investment investment = new Investment(candidateDoc);
                investments.add(investment);
            }
        } else {
            investments = new ArrayList<>();
        }
        this.instance = instance;
    }

    public void save() {
        Document document = new Document();
        document.append("userId", userId);
        document.append("messagesSent", messagesSent);
        document.append("isDumaMember", isDumaMember);
        document.append("roubles", roubles);
        document.append("serverMemberStatus", serverMemberStatus);
        document.append("lastActivityDate", lastActivityDate);
        document.append("investments", investments);
        instance.userCollection.replaceOne(Filters.eq("userId", userId), document, new ReplaceOptions().upsert(true));
    }

    public boolean isBot() {
        try {
            return instance.api.getUserById(userId)
                    .thenApply(user -> user != null && user.isBot())
                    .exceptionally(throwable -> {
                        if (throwable.getCause() instanceof org.javacord.api.exception.NotFoundException) {
                        } else {
                            throwable.printStackTrace();
                        }
                        return false;
                    })
                    .join();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setLastActivityDate(String lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public String getLastActivityDate() {
        return lastActivityDate;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getMessagesSent() {
        return messagesSent;
    }

    public void addMessage(int count) {
        messagesSent += count;
    }

    public void setMessages(int count) {
        messagesSent = count;
    }

    public void addRoubles(int count) {
        roubles += count;
    }

    public void removeRoubles(int count) {
        roubles -= count;
    }

    public Integer getRoubles() {
        return roubles;
    }

    public boolean canAfford(int cost) {
        return roubles >= cost;
    }

    public Boolean isDumaMember() {
        return isDumaMember;
    }

    public void addDumaMember() {
        isDumaMember = true;
    }

    public void removeDumaMember() {
        isDumaMember = false;
    }
}
