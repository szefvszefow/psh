package pl.hexlin.handler;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import pl.hexlin.Instance;
import pl.hexlin.servermember.ServerMember;
import pl.hexlin.servermember.ServerMemberStatus;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class ServerJoinHandler implements ServerMemberJoinListener {
    public final Instance instance;

    public ServerJoinHandler(Instance instance) {
        this.instance = instance;
    }


    @Override
    public void onServerMemberJoin(ServerMemberJoinEvent event) {
        if (!event.getServer().getIdAsString().equalsIgnoreCase("1199446281840492629")) return;
        EmbedBuilder joinMessage = new EmbedBuilder()
                .addField("👋 Nowy użytkownik dołączył do naszej społeczności",
                        "Użytkownik {MEMBER} jest naszym **{MEMBERCOUNT}** użytkownikiem!"
                                .replace("{MEMBER}", event.getUser().getName()).replace("{SERVER}", event.getServer().getName())
                                .replace("{MEMBERCOUNT}", String.valueOf(event.getServer().getMemberCount())))
                .setColor(Color.decode("#B32DFF"))
                .setFooter(event.getServer().getName() + " | By Hexlin 2024")
                .setTimestamp(OffsetDateTime.now().toInstant());

        new MessageBuilder().setEmbed(joinMessage).send(instance.api.getTextChannelById("1199866624623259739").get());
        event.getUser().addRole(instance.api.getRoleById("1201453427541160006").get());


        instance.api.getTextChannelById("1199449402243301567").get().sendMessage(
                """
                # <:witam:1208946638517702656> Powitajmy nowego użytkownika {member} na naszym forum. 
                ### Mamy nadzieję że nie opuścisz nas tak szybko jak tutaj dołączyłeś, bo szukając serwera o polityce trafiłeś na najlepszy! <:like:1200114053469708328><:like:1200114053469708328><:like:1200114053469708328> 
                            
                Wypełnij swój profil rozpoczynając od poglądów politycznych na kanale <#1199945690739122258> i sprawdź kanały takie jak <#1201449997393215509> i <#1212775660372758609>. 
                """.replace("{member}", event.getUser().getMentionTag()));
        ServerMember serverMember = new ServerMember(event.getUser().getId(), 0, instance, false, 0, "Nieznana data");
        if (instance.getServerMemberManager().getServerMemberMap().containsKey(event.getUser().getId())) {
        } else {
            instance.getServerMemberManager().addServerMember(serverMember);
        }
    }
}
