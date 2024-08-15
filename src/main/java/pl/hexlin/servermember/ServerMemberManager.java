package pl.hexlin.servermember;

import org.bson.Document;
import pl.hexlin.Instance;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ServerMemberManager {
    private final Map<Long, ServerMember> serverMemberMap = new HashMap<>();
    private final Instance instance;

    public ServerMemberManager(Instance instance) {
        this.instance = instance;
        instance.userCollection.find().forEach((Consumer<? super Document>) document -> {
            ServerMember serverMember = new ServerMember(document, instance);
            serverMemberMap.putIfAbsent(serverMember.getUserId(), serverMember);
        });
    }

    public void addServerMember(ServerMember serverMember) {
        serverMemberMap.putIfAbsent(serverMember.getUserId(), serverMember);
        serverMember.save();
    }

    public void removeServerMember(ServerMember serverMember) {
        instance.userCollection.find().forEach((Consumer<? super Document>) document -> {
            document.remove(serverMember);
        });
        serverMemberMap.remove(serverMember.getUserId(), serverMember);
        serverMember.save();
    }

    public ServerMember getServerMember(Long userId) {
        return serverMemberMap.get(userId);
    }

    public Map<Long, ServerMember> getServerMemberMap() {
        return serverMemberMap;
    }

    public List<ServerMember> getTopUsersByMessagesSent(int count) {
        return serverMemberMap.values().stream()
                .sorted(Comparator.comparingInt(ServerMember::getMessagesSent).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<ServerMember> getTopUsersByRoubles(int count) {
        return serverMemberMap.values().stream()
                .filter(serverMember -> !serverMember.isBot())
                .sorted(Comparator.comparingInt(ServerMember::getRoubles).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
