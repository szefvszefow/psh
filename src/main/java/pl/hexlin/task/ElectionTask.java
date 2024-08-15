package pl.hexlin.task;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import pl.hexlin.Instance;
import pl.hexlin.servermember.ServerMember;
import pl.hexlin.voting.ElectionManager;
import pl.hexlin.voting.RepresentativeCandidate;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimerTask;

public class ElectionTask extends TimerTask {
    public final Instance instance;

    public ElectionTask(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        ElectionManager representativeElectionManager = instance.representativeElectionManager;
        representativeElectionManager.electionMap.values().forEach(election -> {
            if (election.isExpired() && election.isOngoing) {
                election.disableElection();

                instance.getServerMemberManager().getServerMemberMap().forEach((aLong, serverMember) -> {
                    if (serverMember.isDumaMember()) {
                        serverMember.removeDumaMember();
                        instance.api.getUserById(serverMember.getUserId()).join().removeRole(instance.api.getRoleById("1206440253028827146").get());
                    }
                });

                RepresentativeCandidate winnerCandidate = this.getWinner(election.candidates);
                ServerMember serverMember = instance.getServerMemberManager().getServerMember(Long.valueOf(winnerCandidate.userId));
                serverMember.addDumaMember();
                instance.api.getUserById(serverMember.getUserId()).join().addRole(instance.api.getRoleById("1206440253028827146").get());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
                String formattedTime = formatter.format(LocalDateTime.parse(election.timeStarted));

                EmbedBuilder electionEmbed = new EmbedBuilder()
                        .setTitle("**Podsumowanie wyborów z dnia " + formattedTime.replace("T", "-") + "**.")
                        .setImage("https://i.imgur.com/jS8Rar3.png")
                        .setDescription("⏰ Wybory na <@&1206440253028827146> wygrywa kandydat **" + instance.api.getUserById(winnerCandidate.userId).join().getName() + "**. \n\uD83D\uDDF3\uFE0F Czas kadencji wynosi tydzień, potem nastąpią kolejne wybory. ")
                        .setColor(Color.decode("#B32DFF"))
                        .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin", "https://i.imgur.com/1HhOGue.png");

                instance.api.getTextChannelById("1206713642381353070").get().sendMessage(electionEmbed).join();

                election.save();
            }
        });
    }

    public RepresentativeCandidate getWinner(List<RepresentativeCandidate> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        RepresentativeCandidate winner = candidates.get(0);
        for (RepresentativeCandidate candidate : candidates) {
            if (candidate.votes > winner.votes) {
                winner = candidate;
            }
        }

        return winner;
    }
}
