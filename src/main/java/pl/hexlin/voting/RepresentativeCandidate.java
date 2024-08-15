package pl.hexlin.voting;

import org.bson.Document;

public class RepresentativeCandidate {
    public String userId;
    public Integer votes;

    public RepresentativeCandidate(String userId, Integer votes) {
        this.userId = userId;
        this.votes = votes;
    }

    public RepresentativeCandidate(Document document) {
        userId = document.getString("userId");
        votes = document.getInteger("votes");
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVotes(int count) {
        votes += count;
    }
}