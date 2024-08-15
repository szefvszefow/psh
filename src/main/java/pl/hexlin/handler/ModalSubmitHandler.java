package pl.hexlin.handler;

import org.javacord.api.entity.channel.AutoArchiveDuration;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.listener.interaction.ModalSubmitListener;
import pl.hexlin.Instance;
import pl.hexlin.question.Question;
import pl.hexlin.servermember.ServerMember;
import pl.hexlin.voting.Suggestion;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ModalSubmitHandler implements ModalSubmitListener {
    public final Instance instance;

    public ModalSubmitHandler(Instance instance) {
        this.instance = instance;
    }


    @Override
    public void onModalSubmit(ModalSubmitEvent event) {
        switch (event.getModalInteraction().getCustomId()) {
            case "lapamodal" -> {
                String content = event.getModalInteraction().getTextInputValues().get(0);

                if (content.length() > 30) {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Twój tekst przekracza 30 znaków").respond();
                    return;
                }

                if (content.contains("pokaz lape jesli")) {
                    content = content.replace("pokaz lape jesli", "");
                }

                if (content.contains("pokaż łape jeśli")) {
                    content = content.replace("pokaż łape jeśli", "");
                }

                if (content.contains("pokaż lape jeśli")) {
                    content = content.replace("pokaż łape jeśli", "");
                }

                try {
                    File file = instance.graphicsCreator.createLapaimage(event.getModalInteraction().getUser().getIdAsString(), content, "http://hexlin.rsbmw.pl/lapa.png");
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("\uD83D\uDCE2 Ogłoszone przez użytkownika " + event.getInteraction().getUser().getName())
                            .setColor(Color.decode("#36393e"))
                            .setImage(file);

                    instance.api.getTextChannelById("1232027012701491260").get().sendMessage(embed);

                    event.getModalInteraction().createImmediateResponder().respond();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case "guessflagmodal" -> {
                String content = event.getModalInteraction().getTextInputValues().get(0);
                Embed embed0 = instance.api.getMessageById(instance.getQuestHandler().selectedCountry.get(0).messageId, instance.api.getTextChannelById(instance.questChannelId).get()).join().getEmbeds().get(0);

                if (embed0.getDescription().get().contains("⏰ Już ktoś zgadnął poprawną odpowiedź.")) {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Odpowiedź na te pytanie została już zgadnięta.").respond();
                    return;
                }

                if (content.equalsIgnoreCase(instance.getQuestHandler().selectedCountry.get(0).countryName)) {
                    instance.api.getMessageById(instance.getQuestHandler().selectedCountry.get(0).messageId, instance.api.getTextChannelById(instance.questChannelId).get()).thenAccept(message -> {

                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("🗺️ Zgadnij do jakiego państwa należy ta flaga! (Po Polsku)")
                                .setDescription("Pierwsza osoba która zgadnie, dostanie 20 <:euro:1209258743838281829> które można wydać na kanale <#1208952175930114078>. \n\n``⏰ Użytkownik " + event.getInteraction().getUser().getDisplayName(instance.api.getServerById("1199446281840492629").get()) + " odgadnął i jest to " + instance.getQuestHandler().selectedCountry.get(0).getCountryName() + ".``")
                                .setColor(Color.decode("#B32DFF"))
                                .setImage(instance.getQuestHandler().selectedCountry.get(0).getFlagURL())
                                .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024")
                                .setTimestamp(OffsetDateTime.now().toInstant());

                        message.createUpdater().removeAllComponents().removeAllEmbeds().addComponents(ActionRow.of(Button.secondary("guess-flag", "❓", true))).addEmbed(embed).applyChanges();
                        instance.getServerMemberManager().getServerMember(event.getInteraction().getUser().getId()).addRoubles(20);
                        instance.getServerMemberManager().getServerMember(event.getInteraction().getUser().getId()).save();
                        event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("To była prawidłowa odpowiedź, otrzymujesz 20 euro.").respond();

                        new File(instance.getQuestHandler().selectedCountry.get(0).flagDir).delete();
                        instance.getQuestHandler().selectedCountry.clear();
                    });
                }
                event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Ta odpowiedź nie jest prawidłowa.").respond();
            }
            case "guesscapitalmodal" -> {
                String content = event.getModalInteraction().getTextInputValues().get(0);
                Embed embed0 = instance.api.getMessageById(instance.getQuestHandler().selectedCountry.get(0).messageId, instance.api.getTextChannelById(instance.questChannelId).get()).join().getEmbeds().get(0);

                if (embed0.getDescription().get().contains("⏰ Już ktoś zgadnął poprawną odpowiedź.")) {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Odpowiedź na te pytanie została już zgadnięta.").respond();
                    return;
                }

                if (content.equalsIgnoreCase(instance.getQuestHandler().selectedCountry.get(0).capitalName)) {
                    instance.api.getMessageById(instance.getQuestHandler().selectedCountry.get(0).messageId, instance.api.getTextChannelById(instance.questChannelId).get()).thenAccept(message -> {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("🗺️ Zgadnij jaka jest stolica tego państwa! (Po Angielsku)")
                                .setDescription("Pierwsza osoba która zgadnie, dostanie 20 <:euro:1209258743838281829> które można wydać na kanale <#1208952175930114078>. \n\n``⏰ Użytkownik " + event.getInteraction().getUser().getDisplayName(instance.api.getServerById("1199446281840492629").get()) + " odgadnął i jest to " + instance.getQuestHandler().selectedCountry.get(0).getCapitalName() + ".``")
                                .setColor(Color.decode("#B32DFF"))
                                .setImage(instance.getQuestHandler().selectedCountry.get(0).getFlagURL())
                                .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024")
                                .setTimestamp(OffsetDateTime.now().toInstant());

                        message.createUpdater().removeAllComponents().removeAllEmbeds().addComponents(ActionRow.of(Button.secondary("guess-capital", "❓", true))).addEmbed(embed).applyChanges();
                        instance.getServerMemberManager().getServerMember(event.getInteraction().getUser().getId()).addRoubles(20);
                        instance.getServerMemberManager().getServerMember(event.getInteraction().getUser().getId()).save();
                        event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("To była prawidłowa odpowiedź, otrzymujesz 20 euro.").respond();

                        new File(instance.getQuestHandler().selectedCountry.get(0).flagDir).delete();
                        instance.getQuestHandler().selectedCountry.clear();
                    });
                }
                event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Ta odpowiedź nie jest prawidłowa.").respond();
            }
            case "questionmodal" -> {
                String questionContent = event.getModalInteraction().getTextInputValues().get(0);

                EmbedBuilder questionEmbed = new EmbedBuilder()
                        .setTitle("**❓ ● Użytkownik " + event.getInteraction().getUser().getName() + " ogłosił pytanie dnia**")
                        .setDescription(questionContent)
                        .setColor(Color.decode("#B32DFF"))
                        .setImage("http://hexlin.rsbmw.pl/pytanie.png")
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin")
                        .setTimestamp(OffsetDateTime.now().toInstant());

                event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Pomyślnie ogłoszono pytanie dnia.").respond();

                new MessageBuilder().setEmbed(questionEmbed)
                        .addComponents(ActionRow.of(org.javacord.api.entity.message.component.Button.secondary("question-p", "✔\uFE0F Tak (0)"), Button.secondary("question-n", "❌ Nie (0)")))
                        .send(instance.api.getTextChannelById("1218238831392723115").get()).thenAccept(message -> {
                            message.createThread("⚡ Komentarze", AutoArchiveDuration.ONE_DAY);
                            Question question = new Question(message.getIdAsString(), questionContent, event.getInteraction().getUser().getIdAsString(), new ArrayList<>(), instance);
                            instance.getQuestionManager().addQuestion(question);
                            instance.questionManager.questionMap.values().forEach(Question::save);
                        });
            }
            case "creatormodal" -> {
                String flagURL = event.getModalInteraction().getTextInputValues().get(0);
                String dictatorURL = event.getModalInteraction().getTextInputValues().get(1);
                String mapURL = event.getModalInteraction().getTextInputValues().get(2);
                String countryName = event.getModalInteraction().getTextInputValues().get(3);
                String rulingPartyName = event.getModalInteraction().getTextInputValues().get(4);

                System.out.println(flagURL);
                System.out.println(dictatorURL);
                System.out.println(mapURL);
                System.out.println(countryName);
                System.out.println(rulingPartyName);

                event.getInteraction().asModalInteraction().get().respondWithModal("creatormodal1", "Tworzenie grafiki - Part 2",
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "ideologyname", "Nazwa ideologii państwa")),
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "dictatorname", "Nazwa rządzącego")),
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "alliancename", "Nazwa sojuszu")),
                        ActionRow.of(TextInput.create(TextInputStyle.SHORT, "militarycount", "Ilość wojska w M albo K.")));
            }

            case "creatormodal1" -> {
                String ideologyName = event.getModalInteraction().getTextInputValues().get(0);
                String dictatorName = event.getModalInteraction().getTextInputValues().get(1);
                String allianceName = event.getModalInteraction().getTextInputValues().get(2);
                String militaryCount = event.getModalInteraction().getTextInputValues().get(3);

                System.out.println(ideologyName);
                System.out.println(dictatorName);
                System.out.println(allianceName);
                System.out.println(militaryCount);
            }

            case "verificationmodal" -> {
                if (event.getInteraction().getUser().getRoles(event.getInteraction().getServer().get()).contains(instance.api.getRoleById("1199465856032329849").get())) {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Jesteś już zweryfikowany.").respond();
                    return;
                }
                int code = Integer.parseInt(event.getModalInteraction().getTextInputValues().get(0));

                if (code == instance.verificationMap.get(event.getInteraction().getUser().getId())) {
                    event.getInteraction().getUser().removeRole(instance.api.getRoleById("1201453427541160006").get());
                    event.getInteraction().getUser().addRole(instance.api.getRoleById("1199465856032329849").get());
                    event.getInteraction().getUser().addRole(instance.api.getRoleById("1199466385588359218").get());
                    event.getInteraction().getUser().addRole(instance.api.getRoleById("1199831657209942127").get());
                    event.getInteraction().getUser().addRole(instance.api.getRoleById("1199831773505396876").get());
                    event.getInteraction().getUser().addRole(instance.api.getRoleById("1199839852066242623").get());
                    event.getInteraction().getUser().addRole(instance.api.getRoleById("1199856564257964083").get());
                    event.getInteraction().getUser().addRole(instance.api.getRoleById("1199882776070074519").get());
                    event.getInteraction().getUser().addRole(instance.api.getRoleById("1201462352114102313").get());
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Pomyślnie się zweryfikowałeś.").respond();
                    instance.verificationMap.remove(event.getInteraction().getUser().getId());
                    return;
                }
                event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Podałeś niepoprawny kod.").respond();
            }

            case "representantivemodal" -> {
                String suggestionContent = event.getModalInteraction().getTextInputValues().get(0);
                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime desiredTime = currentTime.plusHours(48);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

                String formattedTime = formatter.format(desiredTime);

                LocalDateTime isGoingToExpire = LocalDateTime.parse(formattedTime, formatter);


                EmbedBuilder suggestionEmbed = new EmbedBuilder()
                        .setTitle("**\uD83C\uDF03 ● Reprezentant " + event.getModalInteraction().getUser().getName() + " wnosi do Dumy PSH nową propozycję.**")
                        .addField("\uD83D\uDDFD Ustawa wniesiona przez Reprezentanta Dumy przewiduje: ", suggestionContent)
                        .setDescription("⏰ Głosowanie dobiega końca " + isGoingToExpire + ".")
                        .setImage("https://i.imgur.com/jS8Rar3.png")
                        .setColor(Color.decode("#B32DFF"))
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin", "https://i.imgur.com/1HhOGue.png")
                        .setTimestamp(OffsetDateTime.now().toInstant());

                event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Twoja propozycja została pomyślnie wysłana.").respond();

                new MessageBuilder().setEmbed(suggestionEmbed)
                        .addComponents(ActionRow.of(org.javacord.api.entity.message.component.Button.secondary("p", "✔\uFE0F Popieram (0)"), Button.secondary("n", "❌ Sprzeciw (0)")))
                        .send(instance.api.getTextChannelById("1206714282809626684").get()).thenAccept(message -> {
                            Suggestion suggestion = new Suggestion(message.getIdAsString(), suggestionContent, String.valueOf(isGoingToExpire), event.getModalInteraction().getUser().getIdAsString(), new ArrayList<>(), false, instance);
                            instance.getSuggestionManager().addSuggestion(suggestion);
                            instance.suggestionManager.suggestionMap.values().forEach(Suggestion::save);
                        });
            }

            case "mutemodal1" -> {
                String userId = event.getModalInteraction().getTextInputValues().get(0);
                if (userId.matches("\\d+")) {
                    Long userIdInt = Long.parseLong(userId);
                    if (instance.getServerMemberManager().getServerMemberMap().containsKey(userIdInt)) {
                        ServerMember serverMember = instance.getServerMemberManager().getServerMember(event.getModalInteraction().getUser().getId());
                        User user = instance.api.getUserById(userIdInt).join();

                        if (serverMember.canAfford(30)) {
                            serverMember.removeRoubles(30);
                            event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Pomyślnie dokonałeś/aś zakupu.").respond();
                            user.timeout(event.getInteraction().getServer().get(), Duration.ofMinutes(5)).join();
                            instance.api.getTextChannelById("1199449402243301567").get().sendMessage("<:mocne:1199874474737143911> Użytkownik " + event.getInteraction().getUser().getMentionTag() + " zakupił wyciszenie użytkownika " + user.getMentionTag() + " na 5 minut.");
                        } else {
                            event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie stać Cię na ten zakup.").respond();
                        }
                    } else {
                        event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Ten użytkownik nie jest na serwerze.").respond();
                    }
                } else {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("To nie jest poprawne ID.").respond();
                }
            }

            case "mutemodal2" -> {
                String userId = event.getModalInteraction().getTextInputValues().get(0);
                if (userId.matches("\\d+")) {
                    Long userIdInt = Long.parseLong(userId);
                    if (instance.getServerMemberManager().getServerMemberMap().containsKey(userIdInt)) {
                        ServerMember serverMember = instance.getServerMemberManager().getServerMember(event.getModalInteraction().getUser().getId());
                        User user = instance.api.getUserById(userIdInt).join();

                        if (serverMember.canAfford(200)) {
                            serverMember.removeRoubles(200);
                            event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Pomyślnie dokonałeś/aś zakupu.").respond();
                            user.timeout(event.getInteraction().getServer().get(), Duration.ofHours(1)).join();
                            instance.api.getTextChannelById("1199449402243301567").get().sendMessage("<:mocne:1199874474737143911> Użytkownik " + event.getInteraction().getUser().getMentionTag() + " zakupił wyciszenie użytkownika " + user.getMentionTag() + " na 1 godzinę.");
                        } else {
                            event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie stać Cię na ten zakup.").respond();
                        }
                    } else {
                        event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Ten użytkownik nie jest na serwerze.").respond();
                    }
                } else {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("To nie jest poprawne ID.").respond();
                }
            }

            case "rolemodal" -> {
                String roleName = event.getModalInteraction().getTextInputValues().get(0);
                String hexColor = event.getModalInteraction().getTextInputValues().get(1);
                ServerMember serverMember = instance.getServerMemberManager().getServerMember(event.getModalInteraction().getUser().getId());

                if (roleName == null) {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("To nie jest poprawna nazwa roli.").respond();
                }
                if (hexColor == null) {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie podałeś koloru HEX.").respond();
                    return;
                }

                if (!hexColor.contains("#")) {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Podałeś zły kolor HEX.").respond();
                    return;
                }

                if (serverMember.canAfford(500)) {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Pomyślnie dokonałeś/aś zakupu.").respond();
                    event.getModalInteraction().getUser().addRole(instance.api.getRoleById("1208978716252839966").get()).join();

                    event.getModalInteraction().getServer().get().createRoleBuilder().setName(roleName).setColor(Color.decode(hexColor)).create().thenAccept(role -> event.getModalInteraction().getUser().addRole(role)).join();

                    serverMember.removeRoubles(500);
                } else {
                    event.getModalInteraction().createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("Nie stać Cię na ten zakup.").respond();
                }
            }
        }
    }
}
