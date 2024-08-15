package pl.hexlin.handler;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import pl.hexlin.Instance;
import pl.hexlin.radio.station.Station;
import pl.hexlin.servermember.ServerMember;
import pl.hexlin.servermember.ServerMemberStatus;
import pl.hexlin.task.BlockTask;

import java.awt.*;
import java.time.Duration;
import java.time.OffsetDateTime;

public class SlashCommandHandler implements SlashCommandCreateListener {
    public final Instance instance;

    public SlashCommandHandler(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();
        User user = event.getInteraction().getUser();
        Server server = event.getInteraction().getServer().get();
        ServerMember serverMember = instance.getServerMemberManager().getServerMember(event.getInteraction().getUser().getId());

        switch (interaction.getFullCommandName()) {
            case "notowania" -> {
                return;
            }
            case "zablokujuzytkownika" -> {
                if (!interaction.getUser().getIdAsString().equalsIgnoreCase("541981133500579842")) {
                    event.getInteraction()
                            .createImmediateResponder()
                            .setContent("Tylko hexlin może to zrobić.")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    return;
                }
                String mentionedUser = event.getSlashCommandInteraction().getArgumentMentionableValueByName("uzytkownik").get().getMentionTag();
                String userId = mentionedUser.replace("<@", "").replace(">", "");

                if (BlockTask.blockedUserIds.contains(userId)) {
                    event.getInteraction()
                            .createImmediateResponder()
                            .setContent("Usunięto permanentną blokadę użytkownikowi " + mentionedUser + ".")
                            .respond();
                    BlockTask.blockedUserIds.remove(userId);
                    instance.api.getUserById(userId).join().removeTimeout(instance.api.getServerById("1199446281840492629").get()).join();
                    return;
                }

                BlockTask.blockedUserIds.add(userId);
                instance.api.getUserById(userId).join().timeout(instance.api.getServerById("1199446281840492629").get(), Duration.ofDays(7)).join();
                event.getInteraction()
                        .createImmediateResponder()
                        .setContent("Wysłano użytkownika " + mentionedUser + " na mute który może usunąć tylko właściciel.")
                        .respond();
            }
            case "ustawwiadomosci" -> {
                if (!interaction.getUser().getIdAsString().equalsIgnoreCase("541981133500579842")) {
                    event.getInteraction()
                            .createImmediateResponder()
                            .setContent("Tylko hexlin moze zmieniac liczbe wiadomosci")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    return;
                }
                String mentionedUser = event.getSlashCommandInteraction().getArgumentMentionableValueByName("uzytkownik").get().getMentionTag();
                String userId = mentionedUser.replace("<@", "").replace(">", "");
                int count = event.getSlashCommandInteraction().getOptionByName("ilosc").get().getDecimalValue().get().intValue();

                instance.getServerMemberManager().getServerMember(Long.valueOf(userId)).setMessages(count);

                event.getInteraction()
                        .createImmediateResponder()
                        .setContent("Zmieniono liczbe wiadomosci uzytkownika " + instance.api.getUserById(userId).join().getDiscriminatedName() + " na " + count + ".")
                        .respond();
            }
            case "wizytowka" -> {
                String statusContent = event.getSlashCommandInteraction().getArgumentStringValueByName("content").get();
                String statusImage = event.getSlashCommandInteraction().getArgumentStringValueByName("image").get();

                if (statusContent.length() > 200) {
                    event.getInteraction()
                            .createImmediateResponder()
                            .setContent("Twój status jest dłuższy od 200 znaków.")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    return;
                }

                if (!statusImage.endsWith(".jpg") && !statusImage.endsWith(".png")) {
                    event.getInteraction()
                            .createImmediateResponder()
                            .setContent("Podano nieprawidłowy link do obrazka (upewnij się, że link kończy się na .jpg albo .png).")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                }

                if (serverMember.serverMemberStatus.isEmpty()) {
                    ServerMemberStatus serverMemberStatus = new ServerMemberStatus(statusContent, statusImage);
                    serverMember.serverMemberStatus.add(serverMemberStatus);
                } else {
                    serverMember.serverMemberStatus.get(0).setStatusContent(statusContent);
                    serverMember.serverMemberStatus.get(0).setImageURL(statusImage);
                }

                event.getInteraction()
                        .createImmediateResponder()
                        .setContent("Pomyślnie ustawiłeś swoją wizytówkę, będzie widoczna jak ktoś cię spinguje i zniknie po 5 sekundach.")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
            case "profil" -> {
                if (event.getSlashCommandInteraction().getArgumentMentionableValueByName("user").isPresent()) {
                    String mentionedUser = event.getSlashCommandInteraction().getArgumentMentionableValueByName("user").get().getMentionTag();
                    String userId = mentionedUser.replace("<@", "").replace(">", "");
                    ServerMember mentionedMember = instance.getServerMemberManager().getServerMember(Long.valueOf(userId));
                    User mentionedUserObj = instance.api.getUserById(userId).join();

                    int[] levelThresholds = {10, 300, 500, 1000, 2000, 3000, 4000, 5000};
                    int nextLevelThreshold = -1;

                    for (int threshold : levelThresholds) {
                        if (mentionedMember.getMessagesSent() < threshold) {
                            nextLevelThreshold = threshold;
                            break;
                        }
                    }

                    if (nextLevelThreshold > 5000) {
                        nextLevelThreshold = 0;
                    }

                    int remainingMessages = nextLevelThreshold - mentionedMember.getMessagesSent();

                    if (mentionedMember.getMessagesSent() > 5000) {
                        EmbedBuilder profile = new EmbedBuilder()
                                .setTitle("● Statystyki oznaczonego użytkownika PSH.")
                                .addField("\uD83D\uDC64 Użytkownik " + mentionedUserObj.getName(),
                                        "\uD83D\uDCDD Wysłane wiadomości: " + mentionedMember.getMessagesSent() +
                                                "\n\uD83D\uDCBC Ten użytkownik już odblokował wszystkie możliwe posady." +
                                                "\n\uD83D\uDFE2 Ostatnia aktywność: " + mentionedMember.getLastActivityDate()
                                );

                        String rolesField = getRoleWithEmoji(mentionedUserObj, server, "1199882887328190534") + " - 10" + "\n" +
                                getRoleWithEmoji(mentionedUserObj, server, "1199884716443193424") + " - 300" + "\n" +
                                getRoleWithEmoji(mentionedUserObj, server, "1199882943296966666") + " - 500" + "\n" +
                                getRoleWithEmoji(mentionedUserObj, server, "1199882944320381049") + " - 1000" + "\n" +
                                getRoleWithEmoji(mentionedUserObj, server, "1199882945427677365") + " - 2000" + "\n" +
                                getRoleWithEmoji(mentionedUserObj, server, "1199885331017760919") + " - 3000" + "\n" +
                                getRoleWithEmoji(mentionedUserObj, server, "1200255491813687356") + " - 4000" + "\n" +
                                getRoleWithEmoji(mentionedUserObj, server, "1199468016350535710") + " - 5000" + "\n";

                        profile.addField("\uD83D\uDCAC Odblokowane posady za wiadomości\n", rolesField)
                                .setColor(Color.decode("#B32DFF"))
                                .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin 2024")
                                .setThumbnail(mentionedUserObj.getAvatar().getUrl().toString())
                                .setTimestamp(OffsetDateTime.now().toInstant());

                        profile.addField("<:euro:1209258743838281829>   Ekonomia - Euro ", "Twój stan konta wynosi: " + mentionedMember.roubles + " <:euro:1209258743838281829>");

                        event.getInteraction()
                                .createImmediateResponder()
                                .addEmbed(profile)
                                .respond();
                        return;
                    }

                    EmbedBuilder profile = new EmbedBuilder()
                            .setTitle("● Statystyki oznaczonego użytkownika PSH.")
                            .addField("\uD83D\uDC64 Użytkownik " + mentionedUserObj.getName(),
                                    "\uD83D\uDCDD Wysłane wiadomości: " + mentionedMember.getMessagesSent() +
                                            "\n\uD83D\uDCBC Do następnej posady pozostało Ci jeszcze: " + remainingMessages + " wiadomości" +
                                            "\n\uD83D\uDFE2 Ostatnia aktywność: " + mentionedMember.getLastActivityDate()
                            );

                    String rolesField = getRoleWithEmoji(mentionedUserObj, server, "1199882887328190534") + " - 10" + "\n" +
                            getRoleWithEmoji(mentionedUserObj, server, "1199884716443193424") + " - 300" + "\n" +
                            getRoleWithEmoji(mentionedUserObj, server, "1199882943296966666") + " - 500" + "\n" +
                            getRoleWithEmoji(mentionedUserObj, server, "1199882944320381049") + " - 1000" + "\n" +
                            getRoleWithEmoji(mentionedUserObj, server, "1199882945427677365") + " - 2000" + "\n" +
                            getRoleWithEmoji(mentionedUserObj, server, "1199885331017760919") + " - 3000" + "\n" +
                            getRoleWithEmoji(mentionedUserObj, server, "1200255491813687356") + " - 4000" + "\n" +
                            getRoleWithEmoji(mentionedUserObj, server, "1199468016350535710") + " - 5000" + "\n";

                    profile.addField("\uD83D\uDCAC Odblokowane posady za wiadomości\n", rolesField)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail(mentionedUserObj.getAvatar().getUrl().toString())
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    profile.addField("<:euro:1209258743838281829>   Ekonomia - Euro ", "Twój stan konta wynosi: " + mentionedMember.roubles + " <:euro:1209258743838281829>");

                    event.getInteraction()
                            .createImmediateResponder()
                            .addEmbed(profile)
                            .respond();
                    return;
                }
                int[] levelThresholds = {10, 300, 500, 1000, 2000, 3000, 4000, 5000};
                int nextLevelThreshold = -1;

                for (int threshold : levelThresholds) {
                    if (serverMember.getMessagesSent() < threshold) {
                        nextLevelThreshold = threshold;
                        break;
                    }
                }

                int remainingMessages = nextLevelThreshold - serverMember.getMessagesSent();

                if (serverMember.getMessagesSent() > 5000) {
                    EmbedBuilder profile = new EmbedBuilder()
                            .setTitle("● Twoje statystyki pobytu na PSH.")
                            .addField("\uD83D\uDC64 Użytkownik " + user.getName(),
                                    "\uD83D\uDCDD Wysłane wiadomości: " + serverMember.getMessagesSent() +
                                            "\n\uD83D\uDCBC Odblokowałeś już wszystkie możliwe posady." +
                                            "\n\uD83D\uDFE2 Ostatnia aktywność: " + serverMember.getLastActivityDate()
                            );
                    String rolesField = getRoleWithEmoji(user, server, "1199882887328190534") + " - 10" + "\n" +
                            getRoleWithEmoji(user, server, "1199884716443193424") + " - 300" + "\n" +
                            getRoleWithEmoji(user, server, "1199882943296966666") + " - 500" + "\n" +
                            getRoleWithEmoji(user, server, "1199882944320381049") + " - 1000" + "\n" +
                            getRoleWithEmoji(user, server, "1199882945427677365") + " - 2000" + "\n" +
                            getRoleWithEmoji(user, server, "1199885331017760919") + " - 3000" + "\n" +
                            getRoleWithEmoji(user, server, "1200255491813687356") + " - 4000" + "\n" +
                            getRoleWithEmoji(user, server, "1199468016350535710") + " - 5000" + "\n";

                    profile.addField("\uD83D\uDCAC Odblokowane posady za wiadomości\n", rolesField)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail(event.getInteraction().getUser().getAvatar().getUrl().toString())
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    profile.addField("<:euro:1209258743838281829>   Ekonomia - Euro ", "Twój stan konta wynosi: " + serverMember.roubles + " <:euro:1209258743838281829>");

                    event.getInteraction()
                            .createImmediateResponder()
                            .addEmbed(profile)
                            .respond();
                    return;
                }
                EmbedBuilder profile = new EmbedBuilder()
                        .setTitle("● Twoje statystyki pobytu na PSH.")
                        .addField("\uD83D\uDC64 Użytkownik " + user.getName(),
                                "\uD83D\uDCDD Wysłane wiadomości: " + serverMember.getMessagesSent() +
                                        "\n\uD83D\uDCBC Do następnej posady pozostało Ci jeszcze: " + remainingMessages + " wiadomości" +
                                        "\n\uD83D\uDFE2 Ostatnia aktywność: " + serverMember.getLastActivityDate()
                        );

                String rolesField = getRoleWithEmoji(user, server, "1199882887328190534") + " - 10" + "\n" +
                        getRoleWithEmoji(user, server, "1199884716443193424") + " - 300" + "\n" +
                        getRoleWithEmoji(user, server, "1199882943296966666") + " - 500" + "\n" +
                        getRoleWithEmoji(user, server, "1199882944320381049") + " - 1000" + "\n" +
                        getRoleWithEmoji(user, server, "1199882945427677365") + " - 2000" + "\n" +
                        getRoleWithEmoji(user, server, "1199885331017760919") + " - 3000" + "\n" +
                        getRoleWithEmoji(user, server, "1200255491813687356") + " - 4000" + "\n" +
                        getRoleWithEmoji(user, server, "1199468016350535710") + " - 5000" + "\n";

                profile.addField("\uD83D\uDCAC Odblokowane posady za wiadomości\n", rolesField)
                        .setColor(Color.decode("#B32DFF"))
                        .setFooter(event.getInteraction().getServer().get().getName() + " | By Hexlin 2024")
                        .setThumbnail(event.getInteraction().getUser().getAvatar().getUrl().toString())
                        .setTimestamp(OffsetDateTime.now().toInstant());

                profile.addField("<:euro:1209258743838281829>   Ekonomia - Euro ", "Twój stan konta wynosi: " + serverMember.roubles + " <:euro:1209258743838281829>");

                event.getInteraction()
                        .createImmediateResponder()
                        .addEmbed(profile)
                        .respond();

            }

            case "play" -> {
                if (event.getInteraction().getUser().getConnectedVoiceChannels().isEmpty()) {
                    event.getInteraction()
                            .createImmediateResponder()
                            .setContent("Nie ma cie na kanale głosowym")
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    return;
                }

                String query = event.getSlashCommandInteraction().getOptionByName("link").get().getStringValue().get();

                instance.trackPlayer.loadTrack(event.getInteraction().getChannel().get(), query, event.getInteraction().getUser().getConnectedVoiceChannel(event.getInteraction().getServer().get()).get());

                event.getInteraction()
                        .createImmediateResponder()
                        .respond();
            }

            case "stop" -> {
                EmbedBuilder queue = new EmbedBuilder()
                        .setTitle("Zatrzymano odtwarzanie wszystkich utworów.")
                        .setColor(Color.decode("#B32DFF"))
                        .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024", "https://i.imgur.com/XuIIA1v.png")
                        .setTimestamp(OffsetDateTime.now().toInstant());

                event.getInteraction()
                        .createImmediateResponder()
                        .addEmbed(queue)
                        .respond();
                instance.trackPlayer.stop();
            }

            case "radio" -> {
                String statusContent = event.getSlashCommandInteraction().getArgumentStringValueByName("name").get();
                Station station = instance.getStationManager().getStation(statusContent);

                if (station != null) {
                    EmbedBuilder queue = new EmbedBuilder()
                            .setTitle("Rozpoczęto odtwarzanie " + station.getStationName())
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024", "https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    event.getInteraction()
                            .createImmediateResponder()
                            .addEmbed(queue)
                            .respond();

                    instance.trackPlayer.loadTrack(event.getInteraction().getChannel().get(), station.getStationURL(), event.getInteraction().getUser().getConnectedVoiceChannel(event.getInteraction().getServer().get()).get());
                    return;
                }

                event.getInteraction()
                        .createImmediateResponder()
                        .setContent("Taka stacja radiowa nie istnieje.")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }

            case "skip" -> {
                EmbedBuilder queue = new EmbedBuilder()
                        .setTitle("Pominięto odtwarzanie " + instance.trackPlayer.audioPlayer.getPlayingTrack().getInfo().title)
                        .setColor(Color.decode("#B32DFF"))
                        .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy | By Hexlin 2024", "https://i.imgur.com/XuIIA1v.png")
                        .setTimestamp(OffsetDateTime.now().toInstant());

                event.getInteraction()
                        .createImmediateResponder()
                        .addEmbed(queue)
                        .respond();
                instance.trackPlayer.trackScheduler.nextTrack();
            }
        }
    }

    private String getRoleWithEmoji(User user, Server server, String roleId) {
        Role role = instance.api.getRoleById(roleId).orElse(null);
        if (role != null && user.getRoles(server).contains(role)) {
            return "✅ " + role.getMentionTag();
        } else {
            return "❌ " + role.getMentionTag();
        }
    }
}
