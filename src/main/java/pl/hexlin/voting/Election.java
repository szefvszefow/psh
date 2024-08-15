package pl.hexlin.voting;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.javacord.api.entity.user.User;
import pl.hexlin.Instance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Election {
    public final Instance instance;
    public String electionMessageId;
    public List<RepresentativeCandidate> candidates;
    public List<String> votedUsers;
    public String isGoingToEnd;
    public String timeStarted;
    public Boolean isOngoing;
    public String winnerId;

    public Election(String electionMessageId, List<RepresentativeCandidate> candidates, String isGoingToEnd, String timeStarted, Boolean isOngoing, String winnerId, ArrayList<String> votedUsers, Instance instance) {
        this.electionMessageId = electionMessageId;
        this.candidates = candidates;
        this.isGoingToEnd = isGoingToEnd;
        this.timeStarted = timeStarted;
        this.isOngoing = isOngoing;
        this.winnerId = winnerId;
        this.instance = instance;
        this.votedUsers = votedUsers;
    }

    public Election(Document document, Instance instance) {
        electionMessageId = document.getString("electionMessageId");
        List<Document> candidateDocuments = document.getList("candidates", Document.class);

        candidates = new ArrayList<>();
        for (Document candidateDoc : candidateDocuments) {
            RepresentativeCandidate candidate = new RepresentativeCandidate(candidateDoc);
            candidates.add(candidate);
        }

        isGoingToEnd = document.getString("isGoingToEnd");
        timeStarted = document.getString("timeStarted");
        isOngoing = document.getBoolean("isOngoing");
        winnerId = document.getString("winnerId");
        votedUsers = document.getList("votedUsers", String.class);
        this.instance = instance;
    }

    public void addCandidate(RepresentativeCandidate representativeCandidate) {
        candidates.add(representativeCandidate);
    }

    public void addVotedUser(User user) {
        votedUsers.add(user.getIdAsString());
    }

    public RepresentativeCandidate getCandidate(String userId) {
        return this.candidates.stream()
                .filter(candidate -> candidate.userId.equalsIgnoreCase(userId))
                .findFirst()
                .orElse(null);
    }

    public String getVoteUserById(String targetUserId) {
        for (String user : votedUsers) {
            if (user.equals(targetUserId)) {
                return user;
            }
        }
        return null;
    }

    public void disableElection() {
        isOngoing = false;
    }

    public boolean isExpired() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expireTime = LocalDateTime.parse(isGoingToEnd);
        return currentTime.isAfter(expireTime);
    }

    public void save() {
        Document document = new Document();
        document.append("electionMessageId", electionMessageId);
        document.append("candidates", candidates);
        document.append("isGoingToEnd", isGoingToEnd);
        document.append("timeStarted", timeStarted);
        document.append("isOngoing", isOngoing);
        document.append("winnerId", winnerId);
        document.append("votedUsers", votedUsers);
        instance.electionCollection.replaceOne(Filters.eq("electionMessageId", electionMessageId), document, new ReplaceOptions().upsert(true));
    }
}
