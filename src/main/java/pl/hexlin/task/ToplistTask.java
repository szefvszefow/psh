package pl.hexlin.task;

import org.javacord.api.entity.channel.VoiceChannel;
import org.javacord.api.entity.user.User;
import pl.hexlin.Instance;

import java.util.ArrayList;
import java.util.TimerTask;

public class ToplistTask extends TimerTask {
    public final Instance instance;
    public final ArrayList<VoiceChannel> channelIds;
    public final ArrayList<User> users;


    public ToplistTask(Instance instance) {
        this.instance = instance;
        this.users = new ArrayList<>();
        this.channelIds = new ArrayList<>();

        channelIds.add(instance.api.getVoiceChannelById("1199943564034064404").get());
        channelIds.add(instance.api.getVoiceChannelById("1199944313753309254").get());
        channelIds.add(instance.api.getVoiceChannelById("1199944493609259038").get());
        channelIds.add(instance.api.getVoiceChannelById("1199945086113427466").get());
        channelIds.add(instance.api.getVoiceChannelById("1225034535536955473").get());
    }

    @Override
    public void run() {
        users.clear();
            instance.getServerMemberManager().getTopUsersByMessagesSent(5).forEach(serverMember -> {
                users.add(instance.api.getUserById(serverMember.getUserId()).join());
            });

            instance.api.getServerById("1199446281840492629").get()
                    .getVoiceChannelById(channelIds.get(0).getId()).get()
                    .updateName("\uD83C\uDFC6 " + users.get(0).getDiscriminatedName().replace("#0", "") + " ・ " + instance.getServerMemberManager().getServerMember(users.get(0).getId()).getMessagesSent());
            instance.api.getServerById("1199446281840492629").get()
                    .getVoiceChannelById(channelIds.get(1).getId()).get()
                    .updateName("\uD83C\uDF7B " + users.get(1).getDiscriminatedName().replace("#0", "") + " ・ " + instance.getServerMemberManager().getServerMember(users.get(1).getId()).getMessagesSent());
            instance.api.getServerById("1199446281840492629").get()
                    .getVoiceChannelById(channelIds.get(2).getId()).get()
                    .updateName("\uD83C\uDF89 " + users.get(2).getDiscriminatedName().replace("#0", "") + " ・ " + instance.getServerMemberManager().getServerMember(users.get(2).getId()).getMessagesSent());
            instance.api.getServerById("1199446281840492629").get()
                    .getVoiceChannelById(channelIds.get(3).getId()).get()
                    .updateName("\uD83C\uDF8A " + users.get(3).getDiscriminatedName().replace("#0", "") + " ・ " + instance.getServerMemberManager().getServerMember(users.get(3).getId()).getMessagesSent());
        instance.api.getServerById("1199446281840492629").get()
                .getVoiceChannelById(channelIds.get(4).getId()).get()
                .updateName("\uD83E\uDD42 " + users.get(4).getDiscriminatedName().replace("#0", "") + " ・ " + instance.getServerMemberManager().getServerMember(users.get(4).getId()).getMessagesSent());
    }
}
