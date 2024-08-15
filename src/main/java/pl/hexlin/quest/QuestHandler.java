package pl.hexlin.quest;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import pl.hexlin.Instance;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class QuestHandler {
    public final Instance instance;
    public final HashMap<String, String> countryNames;
    public final ArrayList<Country> selectedCountry;

    public QuestHandler(Instance instance) {
        this.instance = instance;
        this.countryNames = new HashMap<>();
        this.selectedCountry = new ArrayList<>();
        this.innitMaps();
    }
    public void guessCountryFlag() {
        if (!selectedCountry.isEmpty()) {
            System.out.println(selectedCountry.get(0).getFlagURL());
            return;
        }
        this.getRandomCountry();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🗺️ Zgadnij do jakiego państwa należy ta flaga! (Po Polsku)")
                .setDescription("Pierwsza osoba która zgadnie, dostanie 20 <:euro:1209258743838281829> które można wydać na kanale <#1208952175930114078>..")
                .setColor(Color.decode("#B32DFF"))
                .setImage(new File(instance.getQuestHandler().selectedCountry.get(0).flagDir))
                .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024")
                .setTimestamp(OffsetDateTime.now().toInstant());
        new MessageBuilder().setEmbed(embed)
                .addComponents(ActionRow.of(Button.secondary("guess-flag", "❓")))
                .send(instance.api.getTextChannelById(instance.questChannelId).get()).thenAccept(message -> {
            selectedCountry.get(0).setMessageId(message.getIdAsString());
        });
    }

    public void guessCountryCapital() {
        if (!selectedCountry.isEmpty()) {
            System.out.println(selectedCountry.get(0).getFlagURL());
            return;
        }
        this.getRandomCountry();

        System.out.println(instance.getQuestHandler().selectedCountry.get(0).flagDir);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("🗺️ Zgadnij jaka jest stolica tego państwa! (Po Angielsku)")
                .setDescription("Pierwsza osoba która zgadnie, dostanie 20 <:euro:1209258743838281829> które można wydać na kanale <#1208952175930114078>.")
                .setColor(Color.decode("#B32DFF"))
                .setImage(new File(instance.getQuestHandler().selectedCountry.get(0).flagDir))
                .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024")
                .setTimestamp(OffsetDateTime.now().toInstant());
        new MessageBuilder().setEmbed(embed)
                .addComponents(ActionRow.of(Button.secondary("guess-capital", "❓")))
                .send(instance.api.getTextChannelById(instance.questChannelId).get()).thenAccept(message -> {
            selectedCountry.get(0).setMessageId(message.getIdAsString());
        });
    }

    public void innitMaps() {
        String[] countryCodes = Locale.getISOCountries();
        for (String countryCode : countryCodes) {
            Locale obj = new Locale("", countryCode);
            this.countryNames.put(obj.getCountry(), obj.getDisplayCountry());
        }
    }

    public void getRandomCountry() {
        int index = new Random().nextInt(countryNames.size());
        String countryCode = countryNames.keySet().toArray(new String[0])[index];
        Locale countryName = new Locale("", countryCode);

        try {
            this.instance.getApiHandler().retreiveCountry(countryCode, countryName.getDisplayCountry(), selectedCountry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}