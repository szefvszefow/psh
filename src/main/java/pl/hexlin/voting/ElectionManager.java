package pl.hexlin.voting;

import org.bson.Document;
import pl.hexlin.Instance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ElectionManager {
    public final Map<String, Election> electionMap = new HashMap<>();
    public final Instance instance;

    public ElectionManager(Instance instance) {
        this.instance = instance;
        instance.electionCollection.find().forEach((Consumer<? super Document>) document -> {
            Election election = new Election(document, instance);
            electionMap.putIfAbsent(election.electionMessageId, election);
        });
    }

    public void addElections(Election election) {
        electionMap.putIfAbsent(election.electionMessageId, election);
        election.save();
    }

    public void addCandidate(RepresentativeCandidate representativeCandidate) {
        electionMap.values().forEach(election -> {
            election.addCandidate(representativeCandidate);
            election.save();
        });
    }

    public void startElections(String messageId) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime desiredTime = currentTime.plusHours(48);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

        String formattedTime = formatter.format(desiredTime);

        LocalDateTime isGoingToExpire = LocalDateTime.parse(formattedTime, formatter);
        addElections(new Election(messageId, new ArrayList<>(), String.valueOf(isGoingToExpire), String.valueOf(currentTime), true, "", new ArrayList<>(), instance));
    }

    public Election getElection(String electionId) {
        return electionMap.get(electionId);
    }
}
