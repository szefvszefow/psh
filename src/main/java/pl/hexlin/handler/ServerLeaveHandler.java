package pl.hexlin.handler;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;
import pl.hexlin.Instance;

import java.awt.*;
import java.time.OffsetDateTime;

public class ServerLeaveHandler implements ServerMemberLeaveListener {
    public final Instance instance;

    public ServerLeaveHandler(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void onServerMemberLeave(ServerMemberLeaveEvent event) {
        if (!event.getServer().getIdAsString().equalsIgnoreCase("1199446281840492629")) return;
        EmbedBuilder leaveMessage = new EmbedBuilder()
                .addField("\uD83E\uDDA7 Użytkownik opuścił naszą społeczność",
                        "Użytkownik {MEMBER} został użyszkodnikiem i teraz na serwerze jest **{MEMBERCOUNT}** osób!"
                                .replace("{MEMBER}", event.getUser().getName()).replace("{SERVER}", event.getServer().getName())
                                .replace("{MEMBERCOUNT}", String.valueOf(event.getServer().getMemberCount())))
                .setColor(Color.decode("#B32DFF"))
                .setFooter(event.getServer().getName() + " | By Hexlin 2024")
                .setTimestamp(OffsetDateTime.now().toInstant());


        new MessageBuilder().setEmbed(leaveMessage).send(instance.api.getTextChannelById("1199866624623259739").get());
    }
}
