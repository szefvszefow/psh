package pl.hexlin.task;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import pl.hexlin.Instance;
import pl.hexlin.voting.SuggestionManager;

import java.awt.*;
import java.util.TimerTask;

public class SuggestionTask extends TimerTask {
    public final Instance instance;

    public SuggestionTask(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        SuggestionManager suggestionManager = instance.getSuggestionManager();

        suggestionManager.suggestions.forEach(suggestion -> {
            if (suggestion.isExpired() && !suggestion.isFinished()) {
                suggestion.setFinished();
                User suggestionAuthor = instance.api.getUserById(suggestion.getUserId()).join();

                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**● Reprezentant " + suggestionAuthor.getName() + " wnosi do Dumy PSH nową propozycję.**")
                        .addField("\uD83D\uDDFD Ustawa wniesiona przez Reprezentanta Dumy przewiduje następujące:", suggestion.content)
                        .setImage("https://i.imgur.com/jS8Rar3.png")
                        .setDescription("⏰ To głosowanie dobiegło już końca.")
                        .setColor(Color.decode("#B32DFF"))
                        .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin", "https://i.imgur.com/1HhOGue.png");

                instance.api.getMessageById(suggestion.getId(), instance.api.getTextChannelById("1205126338495905792").get()).thenAccept(message -> {
                    message.createUpdater()
                            .removeAllEmbeds()
                            .removeAllComponents()
                            .setEmbed(suggestionEmbed)
                            .addComponents(ActionRow.of(org.javacord.api.entity.message.component.Button.secondary("p", "✔\uFE0F Popieram (" + suggestion._for + ")"), Button.secondary("n", "❌ Sprzeciw (" + suggestion.against + ")")))
                            .applyChanges();
                });
            }
        });
    }
}
