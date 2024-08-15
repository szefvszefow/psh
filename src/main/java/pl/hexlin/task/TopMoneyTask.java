package pl.hexlin.task;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import pl.hexlin.Instance;
import pl.hexlin.servermember.ServerMember;

import java.awt.*;
import java.util.List;
import java.util.TimerTask;

public class TopMoneyTask extends TimerTask {
    public final Instance instance;

    public TopMoneyTask(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        EmbedBuilder suggestionEmbed = new EmbedBuilder()
                .setTitle("**<:euro:1209258743838281829> ● TOP Najbogatszych Użytkowników PSH**")
                .setDescription("")
                .setColor(Color.decode("#B32DFF"))
                .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin", "https://i.imgur.com/1HhOGue.png");

        List<ServerMember> topUsers = instance.serverMemberManager.getTopUsersByRoubles(10);

        StringBuilder descriptionBuilder = new StringBuilder();

        String[] numberEmojis = {"1️⃣", "2️⃣", "3️⃣", "4️⃣", "5\uFE0F⃣", "6\uFE0F⃣", "7\uFE0F⃣", "8\uFE0F⃣", "9\uFE0F⃣", "\uD83D\uDD1F"};


        for (int i = 0; i < topUsers.size(); i++) {
            ServerMember user = topUsers.get(i);
            User discordUser = instance.api.getUserById(user.getUserId())
                    .exceptionally(throwable -> {
                        if (throwable.getCause() instanceof org.javacord.api.exception.NotFoundException) {
                            System.out.println("User not found: ");
                        }
                        return null;
                    })
                    .join();
            if (discordUser != null) {
                descriptionBuilder.append("\n")
                        .append(numberEmojis[i])
                        .append(" ")
                        .append(instance.api.getUserById(discordUser.getId()).join().getName())
                        .append(" | ")
                        .append("**" + user.getRoubles() + "**")
                        .append(" <:euro:1209258743838281829> ");
            }
        }

        suggestionEmbed.setDescription(descriptionBuilder.toString());

        instance.api.getMessageById("1208985200881827861", instance.api.getTextChannelById("1208957449214758943").get()).thenAccept(message -> {
            message.createUpdater()
                    .removeAllEmbeds()
                    .setEmbed(suggestionEmbed)
                    .applyChanges();
        });
    }
}
