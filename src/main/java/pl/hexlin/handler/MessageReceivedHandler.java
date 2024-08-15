package pl.hexlin.handler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.javacord.api.entity.channel.AutoArchiveDuration;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import pl.hexlin.Instance;
import pl.hexlin.gfx.GfxProcess;
import pl.hexlin.gfx.GfxStage;
import pl.hexlin.servermember.ServerMember;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageReceivedHandler implements MessageCreateListener {
    public final Instance instance;
    public Random random;
    private final Cache<String, Long> COOLDOWN_MAP = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();
    private final long cooldownTime = TimeUnit.SECONDS.toMillis(10L);

    public MessageReceivedHandler(Instance instance, Random random) {
        this.instance = instance;
        this.random = random;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageAuthor().getIdAsString().equalsIgnoreCase("735147814878969968")) {
            Pattern pattern = Pattern.compile("<@!?\\d+>");
            Matcher matcher = pattern.matcher(event.getMessageContent());

            while (matcher.find()) {
                String mention = matcher.group();
                String userId = mention.replaceAll("[^\\d.]", "");

                instance.getServerMemberManager().getServerMember(Long.valueOf(userId)).addRoubles(20);
                instance.getServerMemberManager().getServerMember(Long.valueOf( userId)).save();
            }
        }

        if (event.getMessageAuthor().isYourself()) {
            return;
        }

        if (event.getMessageAuthor().isBotUser()) {
            return;
        }

        if (event.getMessageAuthor().getDiscriminatedName().contains("PSH")) {
            return;
        }
        if (event.getMessageAuthor().getDiscriminatedName().equalsIgnoreCase("Vision")) {
            return;
        }

        if (event.getMessageAuthor().getIdAsString().equalsIgnoreCase("960579015054360616") && event.getMessageContent().contains("<@541981133500579842>")) {
            event.getMessage().delete();
            return;
        }

        if (!event.getChannel().getIdAsString().equalsIgnoreCase("1199459264025079858") || !event.getChannel().getIdAsString().equalsIgnoreCase("1210408376912191568") || !event.getChannel().getIdAsString().equalsIgnoreCase("1222053317136617545")) {
            String messageContent = event.getMessageContent();
            Pattern pattern = Pattern.compile("<@!?\\d+>");
            Matcher matcher = pattern.matcher(messageContent);

            while (matcher.find()) {
                String mention = matcher.group();
                String userId = mention.replaceAll("[^\\d.]", "");

                if (userId.equalsIgnoreCase("960579015054360616")) {
                    return;
                }

                if (userId.equalsIgnoreCase("723143293445210192")) {
                    EmbedBuilder help = new EmbedBuilder()
                            .setTitle("⚡ Lista komend dostępnych dla użytkowników")
                            .setDescription("""
                                    /wizytowka - Twoja serwerowa wizytówka która jest widoczna jak ktoś Cię spinguje.
                                    /profil - Twoje statystyki wiadomości, saldo euro i odblokowane posady.
                                    /play - Odtwarzanie muzyki z YT na kanale głosowym.
                                    /stop - Zatrzymanie muzyki.
                                    /skip - Pominięcie utworu.
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    event.getChannel().sendMessage(help);
                    return;
                }

                if (!instance.getServerMemberManager().getServerMember(Long.valueOf(userId)).serverMemberStatus.isEmpty()) {
                    EmbedBuilder queue = new EmbedBuilder()
                            .setTitle("\uD83D\uDCCB Serwerowa wizytówka użytkownika " + instance.api.getUserById(userId).join().getName())
                            .setDescription(instance.getServerMemberManager().getServerMember(Long.valueOf(userId)).serverMemberStatus.get(0).getStatusContent())
                            .setColor(Color.decode("#B32DFF"))
                            .setThumbnail(instance.api.getUserById(userId).join().getAvatar())
                            .setFooter("Zniknie za 5 sekund | By Hexlin 2024", "https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    if (instance.getServerMemberManager().getServerMember(Long.valueOf(userId)).serverMemberStatus.get(0).getImageURL() != null) {
                        queue.setImage(instance.getServerMemberManager().getServerMember(Long.valueOf(userId)).serverMemberStatus.get(0).getImageURL());
                    }


                    if (!event.getChannel().getIdAsString().equalsIgnoreCase("1222053317136617545") || !event.getChannel().getIdAsString().equalsIgnoreCase("1222469995162894337")) {
                        CompletableFuture<Message> messageFuture = event.getMessage().getChannel().sendMessage(queue);
                        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

                        messageFuture.thenAcceptAsync(message -> {
                            scheduler.schedule(() -> {
                                message.delete();
                                scheduler.shutdown();
                            }, 5, TimeUnit.SECONDS);
                        });
                    }
                    break;
                }
            }
        }
        if (event.getMessage().isPrivateMessage() && instance.getGfxProcessManager().getGfxProcess(event.getMessageAuthor().getIdAsString()) != null) {
            GfxProcess gfxProcess = instance.getGfxProcessManager().getGfxProcess(event.getMessageAuthor().getIdAsString());
            PrivateChannel privateChannel = event.getMessage().getPrivateChannel().get();

            if (event.getMessage().getContent().equalsIgnoreCase("anuluj")) {
                instance.getGfxProcessManager().removeGfx(gfxProcess);
                privateChannel.sendMessage("Anulowano.");
                return;
            }

            switch (instance.getGfxProcessManager().getGfxProcess(event.getMessageAuthor().getIdAsString()).getGfxStage()) {
                case STAGE_1 -> {
                    if (isImageURL(event.getMessageContent())) {
                        gfxProcess.setFlagURL(event.getMessageContent());
                        privateChannel.sendMessage("Ustawiono flagę, teraz podaj link do zdjęcia prezydenta.");
                        gfxProcess.setGfxStage(GfxStage.STAGE_2);
                    } else {
                        privateChannel.sendMessage("Podano niepoprawny link (nie kończy się na .png lub .jpg) (Użyj zapodaj.net do obrazków).");
                    }
                }
                case STAGE_2 -> {
                    if (isImageURL(event.getMessageContent())) {
                        gfxProcess.setDictatorURL(event.getMessageContent());
                        privateChannel.sendMessage("Teraz podaj link do obrazka mapy.");
                        gfxProcess.setGfxStage(GfxStage.STAGE_3);
                    } else {
                        privateChannel.sendMessage("Podano niepoprawny link (nie kończy się na .png lub .jpg) (Użyj zapodaj.net do obrazków).");
                    }
                }
                case STAGE_3 -> {
                    if (isImageURL(event.getMessageContent())) {
                        gfxProcess.setMapURL(event.getMessageContent());
                        privateChannel.sendMessage("Teraz podaj nazwę swojego państwa.");
                        gfxProcess.setGfxStage(GfxStage.STAGE_4);
                    } else {
                        privateChannel.sendMessage("Podano niepoprawny link (nie kończy się na .png lub .jpg) (Użyj zapodaj.net do obrazków).");
                    }
                }
                case STAGE_4 -> {
                    if (event.getMessageContent().length() <= 30) {
                        gfxProcess.setCountryName(event.getMessageContent());
                        privateChannel.sendMessage("Teraz podaj nazwę rządzącej partii.");
                        gfxProcess.setGfxStage(GfxStage.STAGE_5);
                    } else {
                        privateChannel.sendMessage("Przekroczono 30 liter, skróć swoją nazwę i wpisz ją ponownie (Użyj zapodaj.net do obrazków).");
                    }
                }
                case STAGE_5 -> {
                    if (event.getMessageContent().length() <= 30) {
                        gfxProcess.setRulingParty(event.getMessageContent());
                        privateChannel.sendMessage("Teraz podaj jak się nazywa prezydent.");
                        gfxProcess.setGfxStage(GfxStage.STAGE_6);
                    } else {
                        privateChannel.sendMessage("Przekroczono 30 liter, skróć swoją nazwę i wpisz ją ponownie (Użyj zapodaj.net do obrazków).");
                    }
                }
                case STAGE_6 -> {
                    if (event.getMessageContent().length() <= 30) {
                        gfxProcess.setDictatorName(event.getMessageContent());
                        privateChannel.sendMessage("Teraz podaj jak się nazywa organizacja sojuszowa tego państwa (Jeżeli brak to wpisz Brak).");
                        gfxProcess.setGfxStage(GfxStage.STAGE_7);
                    } else {
                        privateChannel.sendMessage("Przekroczono 30 liter, skróć swoją nazwę i wpisz ją ponownie.");
                    }
                }
                case STAGE_7 -> {
                    if (event.getMessageContent().length() <= 30) {
                        gfxProcess.setAllianceName(event.getMessageContent());
                        privateChannel.sendMessage("Teraz podaj jak liczna jest armia tego kraju np. (1M).");
                        gfxProcess.setGfxStage(GfxStage.STAGE_8);
                    } else {
                        privateChannel.sendMessage("Przekroczono 30 liter, skróć swoją nazwę i wpisz ją ponownie.");
                    }
                }
                case STAGE_8 -> {
                    if (event.getMessageContent().length() <= 30) {
                        gfxProcess.setMilitaryCount(event.getMessageContent());
                        privateChannel.sendMessage("Teraz podaj jakiej ideologii jest ten kraj.");
                        gfxProcess.setGfxStage(GfxStage.STAGE_9);
                    } else {
                        privateChannel.sendMessage("Przekroczono 30 liter, skróć swoją nazwę i wpisz ją ponownie.");
                    }
                }
                case STAGE_9 -> {
                    if (event.getMessageContent().length() <= 30) {
                        gfxProcess.setIdeologyName(event.getMessageContent());
                        privateChannel.sendMessage("Trwa tworzenie twojej grafiki (Zaraz zostanie wysłana)...");
                        gfxProcess.setGfxStage(GfxStage.STAGE_10);
                        try {
                            File file = instance.graphicsCreator.createMapImage(event.getMessageAuthor().getIdAsString(), gfxProcess.getFlagURL(), gfxProcess.getDictatorURL(), gfxProcess.getMapURL(), gfxProcess.getCountryName(), gfxProcess.getRulingParty(), gfxProcess.getIdeologyName(), gfxProcess.getDictatorName(), gfxProcess.getAllianceName(), gfxProcess.getMilitaryCount());
                            gfxProcess.setFile(file);
                            privateChannel.sendMessage(file);
                            privateChannel.sendMessage("Tak wygląda twoja grafika. Czy chcesz ją opublikować na kanale? Odpowiedz Tak albo Nie.");
                        } catch (IOException | FontFormatException e) {
                            privateChannel.sendMessage("Wystąpił bład w tworzeniu grafiki, prawdopodobnie przez to że uzyłeś imgura do obrazków. Spróbuj ponownie. ``" + e.getMessage() + "``");
                            instance.getGfxProcessManager().removeGfx(gfxProcess);
                            throw new RuntimeException(e);
                        }
                    } else {
                        privateChannel.sendMessage("Przekroczono 30 liter, skróć swoją nazwę i wpisz ją ponownie.");
                    }
                }

                case STAGE_10 -> {
                    if (event.getMessageContent().equalsIgnoreCase("tak")) {
                        Long teleportToSpawn = COOLDOWN_MAP.getIfPresent(event.getMessageAuthor().getIdAsString());
                        if (teleportToSpawn != null && teleportToSpawn >= System.currentTimeMillis()) {
                            privateChannel.sendMessage("Następna grafikę będziesz mógł opublikować za: "+ instance.convertTime(cooldownTime));
                            return;
                        }

                        COOLDOWN_MAP.put(event.getMessageAuthor().getIdAsString(), System.currentTimeMillis() + TimeUnit.SECONDS
                                .toMillis(10L));
                        instance.getWebhookManager().sendCountryGraphic(event.getMessageAuthor().asUser().get(), gfxProcess.file);
                        instance.getGfxProcessManager().removeGfx(gfxProcess);
                    }

                    if (event.getMessageContent().equalsIgnoreCase("nie")) {
                        privateChannel.sendMessage("Okej zakończono proces, możesz ponownie odpalić kreator przyciskiem na serwerze.");
                        instance.getGfxProcessManager().removeGfx(gfxProcess);
                    }
                }
            }
        }

        if (instance.getServerMemberManager().getServerMember(event.getMessageAuthor().getId()) == null) {
            instance.getServerMemberManager().addServerMember(new ServerMember(event.getMessageAuthor().getId(), 1, instance, false, 0, "Nieznana data."));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = formatter.format(currentTime);

        instance.getServerMemberManager().getServerMember(event.getMessageAuthor().getId()).addMessage(1);
        instance.getServerMemberManager().getServerMember(event.getMessageAuthor().getId()).setLastActivityDate(formattedTime);

        int randomNumber = random.nextInt(100);

        if (randomNumber <= 30) {
            instance.getServerMemberManager().getServerMember(event.getMessageAuthor().getId()).addRoubles(1);
        }
        instance.getServerMemberManager().getServerMember(event.getMessageAuthor().getId()).save();

        switch (instance.getServerMemberManager().getServerMember(event.getMessageAuthor().getId()).getMessagesSent()) {
            case 10 ->
                    levelUp(event.getMessageAuthor().asUser().get(), event.getServer().get(), "1199882887328190534", event.getChannel());
            case 300 ->
                    levelUp(event.getMessageAuthor().asUser().get(), event.getServer().get(), "1199884716443193424", event.getChannel());
            case 500 ->
                    levelUp(event.getMessageAuthor().asUser().get(), event.getServer().get(), "1199882943296966666", event.getChannel());
            case 1000 ->
                    levelUp(event.getMessageAuthor().asUser().get(), event.getServer().get(), "1199882944320381049", event.getChannel());
            case 2000 ->
                    levelUp(event.getMessageAuthor().asUser().get(), event.getServer().get(), "1199882945427677365", event.getChannel());
            case 3000 ->
                    levelUp(event.getMessageAuthor().asUser().get(), event.getServer().get(), "1199885331017760919", event.getChannel());
            case 4000 ->
                    levelUp(event.getMessageAuthor().asUser().get(), event.getServer().get(), "1200255491813687356", event.getChannel());
            case 5000 ->
                    levelUp(event.getMessageAuthor().asUser().get(), event.getServer().get(), "1199468016350535710", event.getChannel());
        }

        switch (String.valueOf(event.getChannel().getId())) {
            case "1199458915600060557", "1199463373381845084", "1202983158216265760", "1216526698074345512", "1216527662265274478" -> {
                if (event.getMessage().getAttachments().isEmpty()) {
                    return;
                }

                event.getMessage().addReaction("mocne:1199874474737143911");
                event.getMessage().addReaction("gowno:1199874959359606804");
            }

            case "1199459264025079858" -> {
                if (event.getMessage().getAttachments().isEmpty()) {
                    event.getMessage().delete();
                    return;
                }

                if (event.getMessage().getEmbeds().isEmpty()) {
                    event.getMessage().addReaction("mocne:1199874474737143911");
                    event.getMessage().addReaction("gowno:1199874959359606804");
                }
            }

            case "1232027012701491260" -> {
                if (event.getMessage().getAttachments().isEmpty()) {
                    event.getMessage().delete();
                    return;
                }

                event.getMessage().createThread("\uD83D\uDCAC Komentarze", AutoArchiveDuration.ONE_WEEK);

                CustomEmoji r1 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874474737143911").get();
                CustomEmoji r2 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1199874959359606804").get();

                event.getMessage().addReaction(r1);
                event.getMessage().addReaction(r2);
            }
        }

        if (event.getMessageAuthor().getIdAsString().equalsIgnoreCase("541981133500579842")) {
            switch (event.getMessageContent()) {
                case "!t" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83C\uDF7B ● Quiz")
                            .setDescription("""
                                    Rozpocznij quiz zgadywania flagi.
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setTimestamp(OffsetDateTime.now().toInstant());


                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(Button.secondary("quiz-1", "Rozpocznij")))
                            .send(event.getChannel());

                    EmbedBuilder verify2 = new EmbedBuilder()
                            .setTitle("\uD83C\uDF7B ● Quiz")
                            .setDescription("""
                                    Rozpocznij quiz zgadywania stolicy.
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setTimestamp(OffsetDateTime.now().toInstant());


                    new MessageBuilder().setEmbed(verify2)
                            .addComponents(ActionRow.of(Button.secondary("quiz-2", "Rozpocznij")))
                            .send(event.getChannel());
                }
                case "!pokazlape" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83D\uDCF1 ● Pokaż łapę jeśli")
                            .setDescription("""
                                    Zabawa polega na kliknięcie poniższego przycisku i wpisanie tam z jakiego powodu użytkownik albo ty masz pokazać pięść, wyśle się obrazek z Zełenskim
                                    z napisem "pokaż łapę jeśli" i poniżej twój tekst. Twoim albo innego użytkownika zadaniem, jeżeli zgadza się z "jeśli" jest zrobienie zdjęcia tylko i wyłącznie łapy z widocznym obrazkiem na ekranie.
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setImage("http://hexlin.rsbmw.pl/lapa.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(Button.secondary("lapa", "\uD83D\uDCF1")))
                            .send(event.getChannel());
                }
                case "!testpolityczny" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83C\uDF7B ● Test poglądów politycznych")
                            .setDescription("""
                                    Rozwiąż ten test zawierający 44 pytań który dobierze przybliżone do twoich odpowiedzi ideologie, nada ci automatycznie ich role i wyśle wyniki na kanał <#1199458915600060557>.
                                    Jeżeli test nada ci ideologie których nie popierasz, będziesz je mógł usunąć znajdując je wyżej na liście i klikając przycisk.
                                    (Jeżeli robisz test dla zabawy, jest możliwość kliknięcia na końcu że nie zostaną nadane ci role ale wysłana grafika z wynikami).
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setTimestamp(OffsetDateTime.now().toInstant());


                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(Button.secondary("politicaltest", "❓ Sprawdź swoje poglądy!")))
                            .send(event.getChannel());
                }
                case "!banderowiec" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83C\uDF7B ● Test na Banderowca")
                            .setDescription("""
                                    Rozwiąż ten test aby sprawdzić czy kwalifikujesz się na prawdziwego Ukraińskiego banderowca. 
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    Server emojiServer2 = event.getApi().getServerById("943488880353619968").get();
                    CustomEmoji emoji_x0005 = emojiServer2.getCustomEmojiById("1204971416278274109").get();

                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(Button.secondary("banderowiecquiz", "Czy jestem prawdziwym banderowcem?", emoji_x0005)))
                            .send(event.getChannel());
                }
                case "!goidownik" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83C\uDF7B ● Test na Goidownika")
                            .setDescription("""
                                    Rozwiąż ten test aby sprawdzić czy kwalifikujesz się na prawdziwego Rosyjskiego Goidownika. 
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    Server emojiServer2 = event.getApi().getServerById("943488880353619968").get();
                    CustomEmoji emoji_x0005 = emojiServer2.getCustomEmojiById("1204971390848077824").get();

                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(Button.secondary("goidaquiz", "Czy jestem prawdziwym goidownikiem?", emoji_x0005)))
                            .send(event.getChannel());
                }
                case "!narodowiec" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83C\uDF7B ● Test na Narodowca")
                            .setDescription("""
                                    Rozwiąż ten test aby sprawdzić czy kwalifikujesz się na prawdziwego narodowca. 
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    Server emojiServer2 = event.getApi().getServerById("1199446281840492629").get();
                    CustomEmoji emoji_x0005 = emojiServer2.getCustomEmojiById("1229566728300793866").get();

                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(Button.secondary("narodowiecquiz", "Czy jestem prawdziwym narodowcem?", emoji_x0005)))
                            .send(event.getChannel());
                }
                case "!g" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83C\uDF7B ● Zezwolenie na pingi")
                            .setDescription("""
                                    Jeżeli chcesz pomóc w rozwoju serwera, kliknij przycisk poniżej aby dostawać czasami ping na czacie kiedy zaczniemy jakiś ciekawy temat aby utrzymać aktywność a w tym samym czasie nie irytować użytkowników którzy tych pingów nie chcą.      
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(Button.secondary("pings", "⚡\uFE0F Chcę być powiadomiony o tematach.")))
                            .send(event.getChannel());
                }
                case "updateav" -> {
                    try {
                        instance.api.updateAvatar(new URL("https://i.imgur.com/MhEvLDX.gif"));
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "countries" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83D\uDDFA\uFE0F ● Kreator Państw")
                            .setDescription("""
                                    Ten kreator pozwala na stworzenie grafiki swojego państwa która zostanie wysłana na ten kanał, upewnij się że wszystkie linki mają końcówke .png albo .jpg.
                                    Wklejanie do kreatora nieodpowiednich obrazków (nsfw albo w złej kolejności) będzie usuwane albo karane, dozwolone są symbole każdych ideologii.
                                                  
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(Button.secondary("creator", "✈\uFE0F Stwórz Swoją Grafikę!")))
                            .send(event.getChannel());
                }

                case "q" -> {
                    instance.getServerMemberManager().getServerMember(event.getMessageAuthor().getId()).addRoubles(500);
                }
                case "!eko" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83C\uDF0E ● Gospodarka PSH - Sklep")
                            .setDescription("""
                                    Serwerową walutą jest Euro <:euro:1209258743838281829> które możesz zdobywać poprzez aktywność na serwerze, każda twoja wiadomość daje 30% szansy na wydropienie 1 euro.
                                    Euro można zdobywać także przez bumpowanie serwera (20<:euro:1209258743838281829> / bump) oraz rozwiązywanie zagadek wysyłanych na czat.
                                    Możesz sprawdzić swój stan konta używając komendy /profil.
                                    Aby zakupić coś z tej listy kliknij przycisk z numerkiem odpowiadającym temu co chcesz kupić.
                                    
                                    1️⃣ Wyciszenie dowolnego użytkownika na 5 minut - 30 <:euro:1209258743838281829>.
                                    2️⃣ Wyciszenie dowolnego użytkownika na godzinę - 200 <:euro:1209258743838281829>.
                                    3️⃣ Twoja własna rola z twoim kolorem i nazwą - 500 <:euro:1209258743838281829>.
                                                  
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(Button.secondary("shop-1", "1\uFE0F⃣"), Button.secondary("shop-2", "2\uFE0F⃣"), Button.secondary("shop-3", "3\uFE0F⃣")))
                            .send(event.getChannel());
                }
                case "g" -> {
                    event.getChannel().sendMessage("""
                            # <:witam:1208946638517702656> Powitajmy nowego użytkownika {member} na naszym forum. 
                            ### Mamy nadzieję że nie opuścisz nas tak szybko jak tutaj dołączyłeś! <:like:1200114053469708328><:like:1200114053469708328><:like:1200114053469708328> 
                            
                            Wypełnij swój profil rozpoczynając od swoich poglądów politycznych na kanale <#1199945690739122258>. 
                            """
                            .replace("{member}", event.getMessageAuthor().asUser().get().getMentionTag()));
                }
                case "!psh" -> {
                    CustomEmoji emoji = instance.api.getCustomEmojiById("1202942352143159307").get();

                    new MessageBuilder()
                            .setContent("Select an option of this list!")
                            .addComponents(
                                    ActionRow.of(SelectMenu.create("cities", "Goida", 1, 1,
                                            Arrays.asList(SelectMenuOption.create("<:flag_ksh:1202942352143159307> Opcja 1", "s", "One", emoji),
                                                    SelectMenuOption.create("<:flag_ksh:1202942352143159307> Opcja 2", "s1", "Two", emoji),
                                                    SelectMenuOption.create("<:flag_ksh:1202942352143159307> Opcja 3", "s2", "Three", emoji)))))
                            .send(event.getChannel());
                }

                case "!duma" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("● Duma PSH - Informacje")
                            .setDescription("""
                                    :flag_eu: Duma to serwerowy system demokracji bezpośredniej w którym Wy decydujecie jakie zmiany albo nowości wystąpią na serwerze poprzez głosowanie bezpośrednie.
                                                                        
                                    Co tydzień do Dumy PSH będą wybory na użytkownika który zostanie <@&1206440253028827146> i zdobędzie tę rolę.
                                    Reprezentant Dumy i administracja będzie w stanie dodawać propozycje zmian albo nowości które zostaną wysłane na kanał <#1206714282809626684> i odbędzie się głosowanie Za ✔️ lub przeciw ❌, w przypadku zdecydowanego przegłosowania większości ta kwestia zostanie rozpatrzona przez <@541981133500579842> i prawdopodobnie wprowadzona w życie.
                                    Na tym kanale będą odbywały się wybory na Reprezentanta, kandydować będą mogły 4 osoby które wykazują się aktywnością (Conajmniej 300 wiadomości) na serwerze a osoba z najwyższą ilością głosów wygra. (W przyszłości prawdopodobnie ulegnie to zmianie ze względu na wielkość naszej społeczności).
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setImage("https://i.imgur.com/jS8Rar3.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify).send(event.getChannel());
                }

                case "!reprezentator" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("● Biuro Reprezentanta")
                            .setDescription("""
                                    Witaj <@&1206440253028827146> w swoim nowym biurze i gratulacje wygranych wyborów!
                                                                        
                                    Klikając przycisk poniżej będziesz mógł opisać swoją propozycję jaką chcesz dodać na serwer (pamiętaj aby opisać profesjonalnie i szczegółowo swój pomysł) a następnie zostanie
                                    ona wysłana na kanał <#1206443449193341020>. Pamiętaj że możesz wstawić tylko 1 propozycję na 24h a wystawianie żartownych propozycji będzie karalne.
                                                                        
                                    To tyle, Powodzenia.                 
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/l4i1S8W.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify).addComponents(ActionRow.of(Button.secondary("representative", "\uD83C\uDF06 Ogłoś Propozycję")))
                            .send(event.getChannel());
                }

                case "!reprezentator-1" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("● Wybory na nowego Reprezentanta Dumy")
                            .setDescription("""
                                    Rozpoczną się wybory które będą trwać 2 dni i 4 osób będzie mogło wziąść udział jako kandydaci.           
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify).addComponents(ActionRow.of(Button.secondary("representative-elections", "\uD83D\uDCBC Ogłoś wybory")))
                            .send(event.getChannel());
                }

                case "!pytanie" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("● Pytanie dnia")
                            .setDescription("""
                                    Rozpocznij pytanie dnia.           
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify).addComponents(ActionRow.of(Button.secondary("question-start", "\uD83D\uDCBC Rozpocznij pytanie")))
                            .send(event.getChannel());
                }


                case "!goida" -> {
                    event.getMessage().delete();

                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("● A Wild Goida Has Appeared")
                            .setDescription("""
                                    GOIDA BRACIA I SIOSTRY GOIDA ZA SMIERC KAPITAŁU WPIERIOD WPIERIOD
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setImage("https://media1.tenor.com/m/3Vc6DZ2R1SEAAAAd/%D0%B3%D0%BE%D0%B9%D0%B4%D0%B0.gif")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify).send(event.getChannel());

                }
                case "!regulamin" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("<:judge:1209266515891851314> ● Krótkie Zasady Społeczności")
                            .setDescription("""                                            
                                    1. Wyzywanie się jest dozwolone ale z dystansem, jeżeli jednak notorycznie będzie to karane.
                                    2. Wysyłanie jakiegokolwiek NSFW na jakiś kanał będzie karane permanentnym banem.
                                    3. Nadmierne pingowanie na <#1199461892641538160> będzie karane.
                                    4. Gloryfikowanie szkodliwych ideii **społecznych** m.in. emo i goth z kultur zachodu nie jest miło widziane. 
                                    5. Z natury nie są mile widziane degeneracyjne i przeseksualizowanie motywy albo użytkownicy.
                                                                                                     
                                    Jest to 5 najbardziej minimalnych i podstawowych zasad z racji że panuje tu wolność słowa.
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setImage("https://i.imgur.com/BfDWaym.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify).send(event.getChannel());
                }

                case "!weryfikacja" -> {
                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("\uD83E\uDD85 ● Weryfikacja")
                            .setDescription("""
                                    Kliknij przycisk "Otrzymaj kod" a potem wpisz otrzymany kod.
                                                                                                
                                    Niestety czasami mogą się zdarzyć krótkie przerwy w działaniu bota, w takim razie proszę o cierpliwość i nie opuszczanie serwera.                                        
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setImage("https://i.imgur.com/6GAQ0IA.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify).addComponents(ActionRow.of(Button.secondary("send", "\uD83D\uDCCB Otrzymaj kod"), Button.success("type", "\uD83D\uDCBC Wpisz kod")))
                            .send(event.getChannel());
                }

                case "!informacje" -> {
                    EmbedBuilder informations = new EmbedBuilder()
                            .setTitle("⛵ ● Informacje o Serwerze")
                            .setDescription("""
                                    Ten krótki opis zamierza podsumować wszystkie możliwe pytania oraz inne ciekawostki na temat tego serwera.
                                                                        
                                    PSH To od niedawna istniejący nowy nowoczesny serwer polityczno-gamingowy z pełną wolnością słowa, sympatyczną atmosferą i luźnymi zasadami,
                                    każdy może rozmawiać na jakikolwiek temat i poszerzać swoje poglądy a także znaleźć kogoś do gry. Często organizowane są przez administrację różne
                                    debaty i inne wydarzenia a także dodawane nowe nowoczesne funkcje do serwera.
                                      
                                    👓 **Od czego jest skrót PSH?**
                                    Polityka, Strategia i Historia.
                                         
                                    🦅 **Po co został stworzony ten serwer gdy są już inne o podobnej tematyce?**
                                    Miałem wrażenie ze inne serwery polityczne są nieestetycznie zrobione i brakuje im nowoczesności a także ciężko o serwer łączący politykę z grami i nie posiadającym ~~Ukraińskiej~~ zpoprawnościowo politycznionej dzieciarskiej administracji, tutaj nie dostaniesz bana za napisanie "kocham faszyzm, lwów jest polski" czy czegokolwiek podobnego. Więc zamierzam dodać swoją cegiełke do tej niszy jako iż polityka i programowanie botów to moje hobby.
                                                                                                      
                                    🐧 **Czy zamierzasz kiedyś nagrywać debaty z tego serwera na YouTube?**
                                    Jeżeli ten serwer zdobędzie jakąś aktywną bazę użytkownikow chętnych do uczestniczenia w debatach głosowych, to może.
                                                                        
                                    Ten serwer nie ma zamiaru propagować jakichkolwiek ideologii, organizacji, czy partii politycznych.
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setImage("https://i.imgur.com/WKNkK6k.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(informations).send(event.getChannel());
                }
            }
            switch (event.getMessageContent()) {
                case "send-personal" -> {
                    Server server2 = event.getApi().getServerById("1214700415736881263").get();
                    CustomEmoji rr1 = server2.getCustomEmojiById("1214700873331249224").get();
                    CustomEmoji rr2 = server2.getCustomEmojiById("1214701612216361082").get();
                    CustomEmoji rr3 = server2.getCustomEmojiById("1214701884800106516").get();
                    CustomEmoji rr4 = server2.getCustomEmojiById("1214701898343383070").get();

                    EmbedBuilder ping = new EmbedBuilder()
                            .setTitle("\uD83C\uDF7B ● Zezwolenie na pingi")
                            .setDescription("""
                                    Jeżeli chcesz pomóc w rozwoju serwera, kliknij przycisk poniżej aby dostawać czasami ping na czacie kiedy zaczniemy jakiś ciekawy temat aby utrzymać aktywność a w tym samym czasie nie irytować użytkowników którzy tych pingów nie chcą.      
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(ping)
                            .addComponents(ActionRow.of(Button.secondary("pings", "⚡\uFE0F Chcę być powiadomiony o tematach.")))
                            .send(event.getChannel());

                    EmbedBuilder gender = new EmbedBuilder()
                            .setTitle("● Role Personalne - Płeć")
                            .setDescription("""
                                    :male_sign: Mężczyzna
                                    :female_sign: Kobieta
                                    \uD83D\uDEB9 Femboy
                                    🦧 Inna
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(gender)
                            .addComponents(ActionRow.of(
                                    Button.secondary("male", "♂"),
                                    Button.secondary("female", "♀\uFE0F"),
                                    Button.secondary("femboy", "\uD83D\uDEB9"),
                                    Button.secondary("other", "\uD83E\uDDA7")
                            ))
                            .send(event.getChannel());

                    EmbedBuilder games = new EmbedBuilder()
                            .setTitle("● Role Personalne - Gry")
                            .setDescription("""
                                    Jeżeli wybierzesz jakąś rolę to będziesz dostawał pingi od osób które szukają kogoś do grania.
                                                                        
                                    <:hoi4:1214700873331249224> Hearts of Iron 4
                                    <:aoh:1214701612216361082> Age of History
                                    <:paradox:1214701884800106516> Inne gry Paradoxu
                                    <:wt:1214701898343383070> War Thunder            
                                    """)
                            .setColor(Color.decode("#8D25FF"));

                    new MessageBuilder().setEmbed(games)
                            .addComponents(ActionRow.of(
                                    Button.secondary("hoi4", rr1),
                                    Button.secondary("aoh", rr2),
                                    Button.secondary("other-paradox", rr3),
                                    Button.secondary("war-thunder", rr4)
                            ))
                            .send(event.getChannel());

                    CustomEmoji r011 = server2.getCustomEmojiById("1214702062785269871").get();
                    CustomEmoji r021 = server2.getCustomEmojiById("1214702319585722479").get();
                    CustomEmoji r031 = server2.getCustomEmojiById("1214702641138114570").get();

                    EmbedBuilder religion = new EmbedBuilder()
                            .setTitle("● Role Personalne - Religia")
                            .setDescription("""
                                                                  
                                    <:christian:1214702062785269871> Wierzący/a
                                    <:atheism:1214702319585722479> Ateista/ka
                                    <:agnosticism:1214702641138114570> Agnostycyzm          
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(religion)
                            .addComponents(ActionRow.of(
                                    Button.secondary("religious", r011),
                                    Button.secondary("ateist", r021),
                                    Button.secondary("agnostic", r031)
                            ))
                            .send(event.getChannel());

                    CustomEmoji kshFlag = event.getServer().get().getCustomEmojiById("1202942352143159307").get();
                    CustomEmoji silFlag = event.getServer().get().getCustomEmojiById("1202942582141747321").get();

                    EmbedBuilder verify = new EmbedBuilder()
                            .setTitle("● Role Personalne - Mniejszości etniczne")
                            .setDescription("""
                                    <:flag_ksh:1202942352143159307> Kaszubi
                                    <:sil:1202942582141747321> Ślązacy
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(verify)
                            .addComponents(ActionRow.of(
                                    Button.secondary("ksh", "", kshFlag),
                                    Button.secondary("sil", "", silFlag)
                            ))
                            .send(event.getChannel());

                    CustomEmoji poland = server2.getCustomEmojiById("1215029448642924674").get();
                    CustomEmoji abroad = server2.getCustomEmojiById("1215029060460216361").get();

                    EmbedBuilder country = new EmbedBuilder()
                            .setTitle("● Role Personalne - Lokalizacja pobytu")
                            .setDescription("""
                                    <:flagmap_pl:1215029448642924674> Mieszkam w Polsce
                                    <:international:1215029060460216361> Przebywam za granicą
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(country)
                            .addComponents(ActionRow.of(
                                    Button.secondary("poland", "", poland),
                                    Button.secondary("abroad", "", abroad)
                            ))
                            .send(event.getChannel());

                    Server server = event.getApi().getServerById("1138661566762455121").get();

                    CustomEmoji rr01 = server.getCustomEmojiById("1202954668787896340").get();
                    CustomEmoji rr02 = server.getCustomEmojiById("1202954815152324648").get();
                    CustomEmoji rr03 = server.getCustomEmojiById("1202954913232191558").get();
                    CustomEmoji rr04 = server.getCustomEmojiById("1202954986430930964").get();
                    CustomEmoji rr05 = server.getCustomEmojiById("1202955071957241867").get();

                    EmbedBuilder regional = new EmbedBuilder()
                            .setTitle("● Role Personalne - Województwa Part 1")
                            .setDescription("""
                                    <:16:1202954668787896340> Dolnośląskie
                                    <:15:1202954815152324648> Kujawsko-pomorskie
                                    <:14:1202954913232191558> Lubelskie
                                    <:13:1202954986430930964> Lubuskie
                                    <:12:1202955071957241867> Łódzkie
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(regional)
                            .addComponents(ActionRow.of(
                                    Button.secondary("Dolnośląskie", "", rr01),
                                    Button.secondary("Kujawsko-pomorskie", "", rr02),
                                    Button.secondary("Lubelskie", "", rr03),
                                    Button.secondary("Lubuskie", "", rr04),
                                    Button.secondary("Łódzkie", "", rr05)
                            ))
                            .send(event.getChannel());

                    CustomEmoji r01 = server.getCustomEmojiById("1202955148268142642").get();
                    CustomEmoji r02 = server.getCustomEmojiById("1202955223174479914").get();
                    CustomEmoji r03 = server.getCustomEmojiById("1202955325670424638").get();
                    CustomEmoji r04 = server.getCustomEmojiById("1202955416527577120").get();
                    CustomEmoji r05 = server.getCustomEmojiById("1202955752210300958").get();

                    EmbedBuilder regional0 = new EmbedBuilder()
                            .setTitle("● Role Personalne - Województwa Part 2")
                            .setDescription("""
                                    <:11:1202955148268142642> Małopolskie
                                    <:10:1202955223174479914> Mazowieckie
                                    <:9_:1202955325670424638> Opolskie
                                    <:8_:1202955416527577120> Podkarpackie
                                    <:7_:1202955752210300958> Podlaskie
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(regional0)
                            .addComponents(ActionRow.of(
                                    Button.secondary("Małopolskie", "", r01),
                                    Button.secondary("Mazowieckie", "", r02),
                                    Button.secondary("Opolskie", "", r03),
                                    Button.secondary("Podkarpackie", "", r04),
                                    Button.secondary("Podlaskie", "", r05)
                            ))
                            .send(event.getChannel());

                    CustomEmoji r001 = server.getCustomEmojiById("1202955856229048371").get();
                    CustomEmoji r002 = server.getCustomEmojiById("1202955967193550848").get();
                    CustomEmoji r003 = server.getCustomEmojiById("1202956322920996864").get();
                    CustomEmoji r004 = server.getCustomEmojiById("1202956405653377044").get();
                    CustomEmoji r005 = server.getCustomEmojiById("1202956501589688340").get();

                    EmbedBuilder regional00 = new EmbedBuilder()
                            .setTitle("● Role Personalne - Województwa Part 3")
                            .setDescription("""
                                    <:6_:1202955856229048371> Pomorskie
                                    <:5_:1202955967193550848> Śląskie
                                    <:4_:1202956322920996864> Świętokrzyskie
                                    <:3_:1202956405653377044> Warmińsko-Mazurskie
                                    <:2_:1202956501589688340> Wielkopolskie
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(regional00)
                            .addComponents(ActionRow.of(
                                    Button.secondary("Pomorskie", "", r001),
                                    Button.secondary("Śląskie", "", r002),
                                    Button.secondary("Świętokrzyskie", "", r003),
                                    Button.secondary("Warmińsko-Mazurskie", "", r004),
                                    Button.secondary("Wielkopolskie", "", r005)
                            ))
                            .send(event.getChannel());

                    CustomEmoji r0005 = server.getCustomEmojiById("1202956571726970880").get();

                    EmbedBuilder regional000 = new EmbedBuilder()
                            .setTitle("● Role Personalne - Województwa Part 4")
                            .setDescription("""
                                    <:1_:1202956571726970880> Zachodniopomorskie
                                    """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(regional000)
                            .addComponents(ActionRow.of(
                                    Button.secondary("Zachodniopomorskie", "", r0005)
                            ))
                            .send(event.getChannel());

                    EmbedBuilder end = new EmbedBuilder()
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setImage("https://i.imgur.com/1ZTpsZV.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(end).send(event.getChannel());
                }
            }
            switch (event.getMessageContent()) {
                case "send-partner" -> {
                    EmbedBuilder parties1 = new EmbedBuilder()
                            .setTitle("● Informacje o partnerstwach")
                            .setDescription("""
                                    Partnerstwa na naszym serwerze najchętniej przyjmujemy w postaci kanał za kanał ponieważ jest to najefektywniejsza metoda partnerstwa przez co nasze serwery mogą być promowane nawzajem przez cały czas.
                                    Bądźmy szczerzy że żadna osoba nie patrzy kanału partnerstwa na jakimś serwerze i czyta ten spam by zobaczyć jakie reklamy są tam wystawione, jest to po prostu bezsensowne.
                                    A jeżeli jest to partnerstwo kanał za kanał i nazwa kanału (czyli serwera) jest widoczna to jest duża szansa że ktoś zobaczy i dołączy.
                                    Polega to na tym że ja robie kanał z nazwa twojego serwera i wklejam tam reklame a ty robisz to samo z nazwą mojego serwera na swoim, upewnij się że na kanale nie można pisać i jest on w dość widocznym miejscu.
                                    
                                    ```
                                    # :eagle:PSH To od niedawna istniejący nowy nowoczesny serwer polityczno-gamingowy.\s
                                    Z polityką pełnej wolności słowa, posiadającym ciekawą i sympatyczną atmosferę oraz wyjątkowo luźne zasady. Każdy może rozmawiać o polityce, wojnie i obecnych wydarzeniach a także znaleźć kogoś do grania w wiele gier strategicznych.\s
                                    Często organizowane są przez administrację różne debaty i inne wydarzenia a także dodawane nowe autorskie funkcje do serwera.
                                                                        
                                    https://i.imgur.com/d8E1Avc.png
                                    https://discord.gg/DXAKsUVk4f
                                    ```

                                    Przyjmuje partnerstwa po zobaczeniu twojego serwera, pamiętaj że musi być on podobny tematycznie do PSH, chociaż minimalnie związanego z grami strategicznymi albo polityką. (Czasami wyjątek mogę zrobić)
                                    Jeżeli jesteś zainteresowany napisz do <@541981133500579842>, nie jestem egotopem zawsze odpisuje.
                                                                        """)
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setImage("https://i.imgur.com/d8E1Avc.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(parties1)
                            .send(event.getChannel());
                }
                case "send-ideologies" -> {
                    Server emojiServer = event.getApi().getServerById("1138661566762455121").get();

                    CustomEmoji emoji_r1 = emojiServer.getCustomEmojiById("1204946560207552552").get();
                    CustomEmoji emoji_r2 = emojiServer.getCustomEmojiById("1204946618319634452").get();
                    CustomEmoji emoji_r3 = emojiServer.getCustomEmojiById("1204946607146016808").get();
                    CustomEmoji emoji_r4 = emojiServer.getCustomEmojiById("1204946597214162944").get();
                    CustomEmoji emoji_r5 = emojiServer.getCustomEmojiById("1204947713385299968").get();

                    EmbedBuilder r1 = new EmbedBuilder()
                            .setTitle("● Ideologie Prawicowe - Part 1")
                            .setDescription("""
                                    <:conservative:1204946560207552552> Konserwatyzm -  To ideologia która polega na utrzymaniu i chronieniu tradycyjnych wartości.
                                    <:traditionalism:1204946618319634452> Tradycjonalizm -  To ideologia która przykłada dużą wagę do znaczenia tradycji, zwyczajów i dziedzictwa kulturowego
                                    <:nationalism:1204946607146016808> Nacjonalizm -  Przekonanie, że naród jest najważniejszą formą uspołecznienia, a tożsamość narodowa najważniejszym składnikiem tożsamości jednostki.
                                    <:monarchism:1204946597214162944> Monarchizm - Monarchizm to ideologia polityczna, która faworyzuje monarchię jako formę rządów.
                                    <:christian_democracy:1204947713385299968> Chrześcijańska Demokracja -  Demokracja, która szczególny nacisk kładzie na rozwój wszelkich form samorządności lokalnej i zasadę pomocniczości.
                                    """)
                            .setColor(Color.decode("#B32DFF"));


                    new MessageBuilder().setEmbed(r1)
                            .addComponents(ActionRow.of(
                                    Button.secondary("convervatism", emoji_r1),
                                    Button.secondary("traditionalism", emoji_r2),
                                    Button.secondary("nationalism", emoji_r3),
                                    Button.secondary("monarchism", emoji_r4),
                                    Button.secondary("christian-democracy", emoji_r5)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_r01 = emojiServer.getCustomEmojiById("1204950581215498260").get();
                    CustomEmoji emoji_r02 = emojiServer.getCustomEmojiById("1204951171027181648").get();
                    CustomEmoji emoji_r03 = emojiServer.getCustomEmojiById("1204951294784180255").get();
                    CustomEmoji emoji_r04 = emojiServer.getCustomEmojiById("1204951417396404314").get();
                    CustomEmoji emoji_r05 = emojiServer.getCustomEmojiById("1204951998479335444").get();

                    EmbedBuilder r2 = new EmbedBuilder()
                            .setTitle("● Ideologie Prawicowe - Part 2")
                            .setDescription("""
                                    <:national_democracy:1204950581215498260> Narodowa Demokracja - Połączenie demokratycznych instytucji z nacjonalistycznymi wartościami.
                                    <:teocracy:1204951171027181648> Teokracja - Doktryna polityczna, według której władzę w państwie sprawuje kapłan lub kapłani.
                                    <:paleolibertarianism:1204951294784180255> Paleolibertarianizm - Łączy libertariańskie podejście do państwa z konserwatyzmem w kulturze i myśli społecznej.
                                    <:classical_liberalism:1204951417396404314> Klasyczny Liberalizm - Nastawienie, że ani rząd, ani żadna grupa czy jednostka społeczna, nie powinny w żaden sposób zakłócać wolności jednostki.
                                    <:representative_democracy:1204951998479335444> Demokracja Pośrednia - Rodzaj demokracji, gdzie decyzje podejmują przedstawiciele społeczeństwa wybrani w wyborach.
                                    """)
                            .setColor(Color.decode("#B32DFF"));
                    new MessageBuilder().setEmbed(r2)
                            .addComponents(ActionRow.of(
                                    Button.secondary("national-democracy", emoji_r01),
                                    Button.secondary("teocracy", emoji_r02),
                                    Button.secondary("paleolibertarianism", emoji_r03),
                                    Button.secondary("classical-liberalism", emoji_r04),
                                    Button.secondary("representative-democracy", emoji_r05)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_r001 = emojiServer.getCustomEmojiById("1204951998479335444").get();
                    CustomEmoji emoji_r002 = emojiServer.getCustomEmojiById("1204953436538535996").get();
                    CustomEmoji emoji_r003 = emojiServer.getCustomEmojiById("1204953560165515264").get();
                    CustomEmoji emoji_r004 = emojiServer.getCustomEmojiById("1204953678906130482").get();
                    CustomEmoji emoji_r005 = emojiServer.getCustomEmojiById("1204953953910132776").get();

                    EmbedBuilder r3 = new EmbedBuilder()
                            .setTitle("● Ideologie Prawicowe - Part 3")
                            .setDescription("""
                                    <:representative_democracy:1204951998479335444> Demokracja Bezpośrednia - System polityczny, w którym decyzje podejmuje się przez głosowanie ludowe
                                    <:corwinism:1204953436538535996> Korwinizm - Zespół postulatów propagowanych przez Korwina-Mikkego.
                                    <:anticommunism:1204953560165515264> Anty-Komunizm - Ideologia uważająca komunizm jako szkodliwy.
                                    <:Euroskepticism:1204953678906130482> Eurosceptycyzm (nie tylko prawicowe, ale w większości) - Pojęcie oznaczające niechęć do większej integracji krajów Unii Europejskiej.
                                    <:antizionism:1204953953910132776> Antysyjonizm - Postawa sprzeciwu wobec ideologii i praktyki syjonizmu niekiedy prowadząca do sprzeciwu wobec samego faktu istnienia państwa Izrael.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(r3)
                            .addComponents(ActionRow.of(
                                    Button.secondary("direct-democracy", emoji_r001),
                                    Button.secondary("korwinism", emoji_r002),
                                    Button.secondary("anti-communism", emoji_r003),
                                    Button.secondary("euro-scepticism", emoji_r004),
                                    Button.secondary("antisyonizm", emoji_r005)
                            ))
                            .send(event.getChannel());

                    Server emojiServer2 = event.getApi().getServerById("943488880353619968").get();
                    CustomEmoji emoji_r0001 = emojiServer2.getCustomEmojiById("1204954740203593778").get();
                    CustomEmoji emoji_r0002 = emojiServer2.getCustomEmojiById("1204954874043699290").get();
                    CustomEmoji emoji_r0003 = emojiServer2.getCustomEmojiById("1204955057624322048").get();

                    EmbedBuilder r4 = new EmbedBuilder()
                            .setTitle("● Ideologie Prawicowe - Part 4")
                            .setDescription("""
                                    <:antilgbt:1204954740203593778> Anty-lgbt - Odrzucenie wszelkich postulatów o równości i uznanie innej orientacji niz hetereosaksualnej za szkodliwą.
                                    <:reactionism:1204954874043699290> Reakcjonizm - Doktryna nawołująca do przywrócenia starego, tradycyjnego porządku społeczno-politycznego
                                    <:prolife:1204955057624322048> Pro-life - Przeciwstawienie się aborcji.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(r4)
                            .addComponents(ActionRow.of(
                                    Button.secondary("anti-lgbt", emoji_r0001),
                                    Button.secondary("reactionism", emoji_r0002),
                                    Button.secondary("pro-life", emoji_r0003)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_r00001 = emojiServer2.getCustomEmojiById("1204955841673830440").get();
                    CustomEmoji emoji_r00002 = emojiServer2.getCustomEmojiById("1204956707126775929").get();
                    CustomEmoji emoji_r00003 = emojiServer2.getCustomEmojiById("1204956257597788230").get();
                    CustomEmoji emoji_r00004 = emojiServer2.getCustomEmojiById("1204956393178927164").get();
                    CustomEmoji emoji_r00005 = emojiServer2.getCustomEmojiById("1204956602642210867").get();

                    EmbedBuilder r5 = new EmbedBuilder()
                            .setTitle("● Ideologie Skrajnie Prawicowe")
                            .setDescription("""
                                    <:fascism:1204955841673830440> Faszyzm -  Faszyzm charakteryzuje się autorytaryzmem, silnym nacjonalizmem, kultem przywódcy, korporacjonizmem ekonomicznym i często ekstremalnym rasizmem.
                                    <:national_socialism:1204956707126775929> Narodowy Socjalizm - Ideologia wąsatego pana z Niemiec.
                                    <:falangism:1204956257597788230> Falangizm/Narodowy Radykalizm - Ideologia polskiej organizacji faszystowskiej.
                                    <:whitesupremacy:1204956393178927164> Biała supremacja - Podkreślanie "wyższości" rasy białej.
                                    <:militarism:1204956602642210867> Militaryzm - System, opierający siłę państwa na stałej i możliwie licznej armii
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(r5)
                            .addComponents(ActionRow.of(
                                    Button.secondary("fascism", emoji_r00001),
                                    Button.secondary("national-socialism", emoji_r00002),
                                    Button.secondary("falangism", emoji_r00003),
                                    Button.secondary("white-supremacy", emoji_r00004),
                                    Button.secondary("militarism", emoji_r00005)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_r000001 = emojiServer2.getCustomEmojiById("1204958168610902076").get();
                    CustomEmoji emoji_r000002 = emojiServer2.getCustomEmojiById("1204958189569974333").get();
                    CustomEmoji emoji_r000003 = emojiServer2.getCustomEmojiById("1204958214937124864").get();
                    CustomEmoji emoji_r000004 = emojiServer2.getCustomEmojiById("1204958236701233222").get();
                    CustomEmoji emoji_r000005 = emojiServer2.getCustomEmojiById("1204958266849890304").get();

                    EmbedBuilder r6 = new EmbedBuilder()
                            .setTitle("● Ideologie Prawicowe Ekonomicznie")
                            .setDescription("""
                                    <:corporatism:1204958168610902076> Korporacjonizm - Doktryna o charakterze samorządu (stanowego, zawodowego, branżowego), postrzegająca społeczeństwo i państwo jako naturalne i solidarne organizmy.
                                    <:capitalism:1204958189569974333> Kapitalizm - System ekonomiczny oparty na prywatnej własności, wolnej konkurencji i rozwiniętej gospodarce rynkowej.
                                    <:leseferism:1204958214937124864> Leseferyzm - Leseferyzm to doktryna, która zakłada minimalną ingerencję państwa w gospodarkę, skupiając się na utrzymaniu prawa i porządku. Jest przeciwieństwem interwencjonizmu.
                                    <:anarchocapitalism:1204958236701233222> Anarchokapitalizm - Libertariański i indywidualistyczny system polityczny, który opowiada się za eliminacją monopolu usług państwa na rzecz prywatnych agencji ochrony na wolnym rynku.
                                    <:freemarket:1204958266849890304> Wolny Rynek - Wolny rynek – rodzaj rynku, na którym wymiana dóbr dokonuje się w wyniku dobrowolnie zawieranych transakcji pomiędzy kupującymi a sprzedającymi, przy dobrowolnie ustalonej przez nich cenie.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(r6)
                            .addComponents(ActionRow.of(
                                    Button.secondary("corportaism", emoji_r000001),
                                    Button.secondary("capitalism", emoji_r000002),
                                    Button.secondary("leseferism", emoji_r000003),
                                    Button.secondary("acap", emoji_r000004),
                                    Button.secondary("market", emoji_r000005)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_l1 = emojiServer2.getCustomEmojiById("1204959545214894160").get();
                    CustomEmoji emoji_l2 = emojiServer2.getCustomEmojiById("1204959765873303602").get();
                    CustomEmoji emoji_l3 = emojiServer2.getCustomEmojiById("1204959862925168650").get();
                    CustomEmoji emoji_l4 = emojiServer2.getCustomEmojiById("1204959972459413514").get();
                    CustomEmoji emoji_l5 = emojiServer2.getCustomEmojiById("1204960118748352562").get();

                    EmbedBuilder l1 = new EmbedBuilder()
                            .setTitle("● Ideologie Lewicowe - Part 1")
                            .setDescription("""
                                    <:social_liberalism:1204959545214894160> Socjalliberalizm - Idea łącząca liberalny wolny rynek, własność prywatną z formą stonowanej pomocy państwa w postaci np. bezpłatnej ochrony zdrowia czy bezpłatnej edukacji.
                                    <:welfare_state:1204959765873303602> Państwo Dobrobytu - Koncepcja państwa stawiająca za cel zabezpieczenie obywateli przed ryzykami wiążącymi się z funkcjonowaniem gospodarki rynkowej.
                                    <:democratic_socialism:1204959862925168650> Socjalizm Demokratyczny -  Opowiada się za socjalizmem jako ustrojem gospodarczym i demokracją jako formą rządów w państwie. Postuluje, aby środki produkcji były w posiadaniu całej populacji.
                                    <:socialdemocracy:1204959972459413514> Socjaldemokracja - Kompromis między uznaniem systemu kapitalistycznego jako najefektywniejszego mechanizmu bogacenia się w strefie prywatnej, a demokracją i dystrybucją tego bogactwa w sposób możliwie najbardziej sprawiedliwy
                                    <:internationalism:1204960118748352562> Internacjonalizm - Ideologia wyrażająca dążenie do równouprawnienia i współpracy wszystkich narodów.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(l1)
                            .addComponents(ActionRow.of(
                                    Button.secondary("socialliberalism", emoji_l1),
                                    Button.secondary("welfare-state", emoji_l2),
                                    Button.secondary("democratic-socialism", emoji_l3),
                                    Button.secondary("socialdemocracy", emoji_l4),
                                    Button.secondary("internationalism", emoji_l5)
                            ))
                            .send(event.getChannel());


                    CustomEmoji emoji_l01 = emojiServer2.getCustomEmojiById("1204960906707206154").get();
                    CustomEmoji emoji_l02 = emojiServer2.getCustomEmojiById("1204961102480277504").get();
                    CustomEmoji emoji_l03 = emojiServer2.getCustomEmojiById("1204961262392320010").get();
                    CustomEmoji emoji_l04 = emojiServer2.getCustomEmojiById("1204961346261876766").get();
                    CustomEmoji emoji_l05 = emojiServer2.getCustomEmojiById("1204961470492975124").get();

                    EmbedBuilder l2 = new EmbedBuilder()
                            .setTitle("● Ideologie Lewicowe - Part 2")
                            .setDescription("""
                                    <:Progressivism:1204960906707206154> Progresywizm - Sposób postrzegania rzeczywistości jako zmiennej w sposób konsekwentny i nieodwracalny
                                    <:prochoice:1204961102480277504> Pro-choice - Ogół ruchów społecznych i organizacji opowiadających się za legalizacją aborcji.
                                    <:cosmopolitanism:1204961262392320010> Kosmopolityzm - pogląd wyrażający brak podlegania podziałom kulturowo-politycznym i terytorialnym.
                                    <:proeu:1204961346261876766> Proeuropeizm - Światopogląd afirmujący koncepcję integracji europejskiej i członkostwo w Unii Europejskiej
                                    <:antigun:1204961470492975124> Anti-gun - Sprzeciwienie się możliwości posiadania bronii.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(l2)
                            .addComponents(ActionRow.of(
                                    Button.secondary("progress", emoji_l01),
                                    Button.secondary("pro-choice", emoji_l02),
                                    Button.secondary("cosmopolitism", emoji_l03),
                                    Button.secondary("pro-eu", emoji_l04),
                                    Button.secondary("anti-gun", emoji_l05)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_l001 = emojiServer2.getCustomEmojiById("1204962143129309224").get();
                    CustomEmoji emoji_l002 = emojiServer2.getCustomEmojiById("1204962205662187620").get();
                    CustomEmoji emoji_l003 = emojiServer2.getCustomEmojiById("1204962278621974590").get();

                    EmbedBuilder l3 = new EmbedBuilder()
                            .setTitle("● Ideologie Lewicowe - Part 3")
                            .setDescription("""
                                    <:socialism:1204962143129309224> Socjalizm - System gospodarczy oparty na społecznej własności środków produkcji.
                                    <:feminism:1204962205662187620> Feminizm - Ruchu o charakterze politycznym, społecznym, kulturowym i intelektualnym, którego różne orientacje, szkoły, teorie i badania łączy wspólne przekonanie, że kobiety były i są przedmiotem dyskryminacji.
                                    <:prolgbt:1204962278621974590> Pro-lgbt - Popieranie równości dla osób LGBT.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(l3)
                            .addComponents(ActionRow.of(
                                    Button.secondary("socialism", emoji_l001),
                                    Button.secondary("feminism", emoji_l002),
                                    Button.secondary("pro-lgbt", emoji_l003)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_l0001 = emojiServer2.getCustomEmojiById("1204962844458881034").get();
                    CustomEmoji emoji_l0002 = emojiServer2.getCustomEmojiById("1204962931180179507").get();
                    CustomEmoji emoji_l0003 = emojiServer2.getCustomEmojiById("1204963009890623609").get();
                    CustomEmoji emoji_l0004 = emojiServer2.getCustomEmojiById("1204963102232416297").get();
                    CustomEmoji emoji_l0005 = emojiServer2.getCustomEmojiById("1204963188341481503").get();
                    CustomEmoji emoji_l0006 = emojiServer2.getCustomEmojiById("1204963987398074411").get();

                    EmbedBuilder ll1 = new EmbedBuilder()
                            .setTitle("● Ideologie Skrajnie Lewicowe - Part 1")
                            .setDescription("""
                                    <:marxism:1204962844458881034> Marksizm - Zespół postulatów propagowanych przez Karola Marksa.
                                    <:communism:1204962931180179507> Komunizm - Ideologia której celem jest utworzenie społeczeństwa pozbawionego ucisku i wyzysku klasowego, opartego na braku własności prywatnej (w rozumieniu marksistowskim).
                                    <:leninism:1204963009890623609> Leninizm - Koncepcja stworzona i rozwijana przez Włodzimierza Lenina jako teoria dostosowująca marksizm do nowej sytuacji historycznej.
                                    <:stalinism:1204963102232416297> Stalinizm - System poglądów Józefa Stalina.
                                    <:nationalbolshevism:1204963188341481503> Narodowy Bolszewizm - Ruch polityczny łączący w sobie elementy nacjonalizmu i bolszewizmu
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(ll1)
                            .addComponents(ActionRow.of(
                                    Button.secondary("marxism", emoji_l0001),
                                    Button.secondary("communism", emoji_l0002),
                                    Button.secondary("leninism", emoji_l0003),
                                    Button.secondary("stalinism", emoji_l0004),
                                    Button.secondary("national-bolshevism", emoji_l0005)
                            ))
                            .send(event.getChannel());

                    EmbedBuilder ll2 = new EmbedBuilder()
                            .setTitle("● Ideologie Skrajnie Lewicowe - Part 2")
                            .setDescription("""
                                    <:trotskyism:1204963987398074411> Trockizm - Nurt komunizmu oparty na poglądach Lwa Trockiego. Trockizm powstał w ZSRR w toku sporu o taktykę partii bolszewickiej i światowego ruchu komunistycznego po śmierci Włodzimierza Lenina.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(ll2)
                            .addComponents(ActionRow.of(
                                    Button.secondary("trotskyism", emoji_l0006)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_x1 = emojiServer2.getCustomEmojiById("1204964309839642755").get();
                    CustomEmoji emoji_x2 = emojiServer2.getCustomEmojiById("1204965699316088903").get();
                    CustomEmoji emoji_x3 = emojiServer2.getCustomEmojiById("1204964554497597471").get();
                    CustomEmoji emoji_x4 = emojiServer2.getCustomEmojiById("1204964675813507132").get();
                    CustomEmoji emoji_x5 = emojiServer2.getCustomEmojiById("1204965057780518932").get();

                    EmbedBuilder other = new EmbedBuilder()
                            .setTitle("● Ideologie Inne - Part 1")
                            .setDescription("""
                                    <:anarchism:1204964309839642755> Anarchizm - doktryna polityczna i ruch społeczny, które cechują się niechęcią wobec władzy.
                                    <:piratepolitics:1204965699316088903> Piracka Polityka - internacjonalny ruch oparty na walce o prawa cyfrowe, wolny dostęp do dóbr kultury, ochronie życia prywatnego (jego prywatności), ochronie tajemnicy korenspodencji i walce z prywatnymi monopolami.
                                    <:centrism:1204964554497597471> Centryzm - Poglądy które można określić jako pośrednie pomiędzy lewicowymi a prawicowymi.
                                    <:protectionism:1204964675813507132> Protekcjonizm - Polityka mająca na celu ochronę oraz promowanie krajowych usług oraz produktów w celu ich wsparcia i rozwoju.
                                    <:distributism:1204965057780518932> Dystrybucjonizm - Postuluje odrzucenie własności wielkokapitalistycznej, zarazem odrzucenie kapitalizmu, ale też odrzuceniem socjalizmu oraz interwencjonizmu.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(other)
                            .addComponents(ActionRow.of(
                                    Button.secondary("anarchism", emoji_x1),
                                    Button.secondary("pirate", emoji_x2),
                                    Button.secondary("centre", emoji_x3),
                                    Button.secondary("protectionism", emoji_x4),
                                    Button.secondary("distributionism", emoji_x5)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_x01 = emojiServer2.getCustomEmojiById("1204966087251333180").get();
                    CustomEmoji emoji_x02 = emojiServer2.getCustomEmojiById("1204966202074726460").get();
                    CustomEmoji emoji_x03 = emojiServer2.getCustomEmojiById("1204967001693618176").get();
                    CustomEmoji emoji_x04 = emojiServer2.getCustomEmojiById("1204966457126166568").get();
                    CustomEmoji emoji_x05 = emojiServer2.getCustomEmojiById("1204966809095372820").get();

                    EmbedBuilder other2 = new EmbedBuilder()
                            .setTitle("● Ideologie Inne - Part 2")
                            .setDescription("""
                                    <:libertarianism:1204966087251333180> Libertarianizm - Polityczna filozofia, która stawia na maksymalną ochronę indywidualnych wolności i minimalną ingerencję państwa w życie jednostki.
                                    <:egalitarism:1204966202074726460> Egalitaryzm - Pogląd zakładający przyrodzoną równość wszystkich ludzi.
                                    <:ecologism:1204967001693618176> Ekologizm - Kierunek filozoficzny podejmujący problemy ochrony środowiska oraz miejsca człowieka w świecie.
                                    <:syndicalism:1204966457126166568> Syndykalizm - Syndykalizm to polityczna i społeczna ideologia, która akcentuje centralną rolę związków zawodowych (syndykatów) w organizacji społeczeństwa i gospodarki.
                                    <:authoritarianism:1204966809095372820> Autorytaryzm - Autorytaryzm to forma rządzenia charakteryzująca się silną, scentralizowaną władzą, gdzie decyzje są podejmowane przez jednostkę lub niewielką grupę osób.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(other2)
                            .addComponents(ActionRow.of(
                                    Button.secondary("libertarianism", emoji_x01),
                                    Button.secondary("egalitarism", emoji_x02),
                                    Button.secondary("ecologism", emoji_x03),
                                    Button.secondary("syndicalism", emoji_x04),
                                    Button.secondary("authoritarism", emoji_x05)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_x001 = emojiServer2.getCustomEmojiById("1204961346261876766").get();
                    CustomEmoji emoji_x002 = emojiServer2.getCustomEmojiById("1204967890323775548").get();
                    CustomEmoji emoji_x003 = emojiServer2.getCustomEmojiById("1204966809095372820").get();
                    CustomEmoji emoji_x004 = emojiServer2.getCustomEmojiById("1204968176299802634").get();

                    EmbedBuilder other3 = new EmbedBuilder()
                            .setTitle("● Ideologie Inne - Part 3")
                            .setDescription("""
                                    <:proeu:1204961346261876766> Paneuropeizm - Ideologia postulująca zjednoczenie wszystkich narodów europy.
                                    <:panslavism:1204967890323775548> Pansłowianizm - Ideologia postulująca zjednoczenie wszystkich narodów słowiańskich.
                                    <:authoritarianism:1204966809095372820> Totalitaryzm - Totalitaryzm to skrajna forma autorytaryzmu, charakteryzująca się kompletnym kontrolowaniem życia społecznego, politycznego i kulturalnego.
                                    <:liberalism:1204968176299802634> Liberalizm - Filozofia polityczna, która kładzie nacisk na ochronę indywidualnych wolności i praw jednostki.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(other3)
                            .addComponents(ActionRow.of(
                                    Button.secondary("paneuropeism", emoji_x001),
                                    Button.secondary("panslovianism", emoji_x002),
                                    Button.secondary("totalitarism", emoji_x003),
                                    Button.secondary("liberalism", emoji_x004)
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_x005 = emojiServer2.getCustomEmojiById("1204971459227820042").get();

                    EmbedBuilder funny = new EmbedBuilder()
                            .setTitle("● Ideologie Bekowe - Part 1")
                            .setDescription("""
                                    🦧 Małpiszonizm
                                    <:z_:1204971459227820042> Ideologia Z (Rosyjski Faszyzm/Putinizm)
                                    🚹 Femboyizm
                                    ❓ Tozależyzm
                                    ♿ Unfonizm
                                    """)
                            .setColor(Color.decode("#B32DFF"));


                    new MessageBuilder().setEmbed(funny)
                            .addComponents(ActionRow.of(
                                    Button.secondary("malpishonism", "\uD83E\uDDA7"),
                                    Button.secondary("z-ideology", emoji_x005),
                                    Button.secondary("femboyism", "\uD83D\uDEB9"),
                                    Button.secondary("that-dependyism", "❓"),
                                    Button.secondary("unfonism", "♿")
                            ))
                            .send(event.getChannel());

                    Server emojiServer00 = event.getApi().getServerById("1199446281840492629").get();

                    CustomEmoji f2 = emojiServer00.getCustomEmojiById("1216680823609102376").get();

                    EmbedBuilder funny2 = new EmbedBuilder()
                            .setTitle("● Ideologie Bekowe - Part 2")
                            .setDescription("""
                                    🍺 Alkoholizm/Piwokracja
                                    <:flag_ua:1216680823609102376> Banderyzm
                                    :rainbow_flag: Pedalizacja Przemysłu
                                    """)
                            .setColor(Color.decode("#B32DFF"));


                    new MessageBuilder().setEmbed(funny2)
                            .addComponents(ActionRow.of(
                                    Button.secondary("alcoholism", "\uD83C\uDF7A"),
                                    Button.secondary("banderism", f2),
                                    Button.secondary("pedalisation", "\uD83C\uDFF3\uFE0F\u200D\uD83C\uDF08")
                            ))
                            .send(event.getChannel());

                    CustomEmoji emoji_x0001 = emojiServer2.getCustomEmojiById("1204968646737404036").get();
                    CustomEmoji emoji_x0002 = emojiServer2.getCustomEmojiById("1204968755466080276").get();
                    CustomEmoji emoji_x0003 = emojiServer2.getCustomEmojiById("1204968921887674378").get();
                    CustomEmoji emoji_x0004 = emojiServer2.getCustomEmojiById("1204971390848077824").get();
                    CustomEmoji emoji_x0005 = emojiServer2.getCustomEmojiById("1204971416278274109").get();

                    EmbedBuilder conflics = new EmbedBuilder()
                            .setTitle("● Stanowisko Militarne")
                            .setDescription("""
                                    <:russia:1204971390848077824> Pro-Rosja
                                    <:ukraine:1204971416278274109> Pro-Ukraina
                                    <:neutral:1204968646737404036> UA-RU Neutralny
                                    <:pronato:1204968755466080276> Pro-Nato
                                    <:antinato:1204968921887674378> Anty-Nato
                                    """)
                            .setColor(Color.decode("#8D25FF"));

                    new MessageBuilder().setEmbed(conflics)
                            .addComponents(ActionRow.of(
                                    Button.secondary("proru", emoji_x0004),
                                    Button.secondary("proua", emoji_x0005),
                                    Button.secondary("net", emoji_x0001),
                                    Button.secondary("nato", emoji_x0002),
                                    Button.secondary("anti-nato", emoji_x0003)
                            ))
                            .send(event.getChannel());

                    Server server = event.getApi().getServerById("1138661566762455121").get();
                    CustomEmoji pis = server.getCustomEmojiById("1202962880513966120").get();
                    CustomEmoji conf = server.getCustomEmojiById("1202962961501782036").get();
                    CustomEmoji left = server.getCustomEmojiById("1202963075477667850").get();
                    CustomEmoji thirdway = server.getCustomEmojiById("1202963169065304154").get();
                    CustomEmoji ko = server.getCustomEmojiById("1202963540017680455").get();

                    EmbedBuilder parties = new EmbedBuilder()
                            .setTitle("● Polskie Partie Polityczne")
                            .setDescription("""
                                    <:pis:1202962880513966120> PIS/Zjednoczona Prawica
                                    <:konfederacja:1202962961501782036> Konfederacja
                                    <:lewica:1202963075477667850> Lewica
                                    <:trzecia_droga:1202963169065304154> Trzecia Droga
                                    <:ko:1202963540017680455> KO
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(parties)
                            .addComponents(ActionRow.of(
                                    Button.secondary("pis", "", pis),
                                    Button.secondary("confederacy", "", conf),
                                    Button.secondary("left", "", left),
                                    Button.secondary("3d", "", thirdway),
                                    Button.secondary("ko", "", ko)
                            ))
                            .send(event.getChannel());

                    EmbedBuilder end = new EmbedBuilder()
                            .setTitle("● Wybierz wszystkie swoje ideologie")
                            .setDescription("Nowe będą dodawane jeżeli jakichś brakuje i jest taka potrzeba.")
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setImage("https://i.imgur.com/iadZOil.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(end).send(event.getChannel());
                }

                case "send-philosophy" -> {
                    EmbedBuilder parties = new EmbedBuilder()
                            .setTitle("● Filozofie - Part 1")
                            .setDescription("""
                                    :one: Stoicyzm -  Filozofia etyczna, która kładzie nacisk na kontrolę nad własnymi reakcjami emocjonalnymi, akceptację nieuniknionych zdarzeń i rozwój cnoty.
                                    :two: Hedonizm - Przekonanie, że najważniejszym celem życia jest osiąganie przyjemności i unikanie cierpienia.
                                    :three: Materializm - Przekonanie, że tylko rzeczy materialne istnieją, a duchowość lub umysł to wynik materii.
                                    :four: Racjonalizm - Pogląd, że pewne prawdy są dostępne ludzkiemu rozumowi bez konieczności doświadczenia.
                                    :five: Egzystencjalizm - Koncentruje się na indywidualnej egzystencji, wolnej woli i odpowiedzialności za swoje wybory.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(parties)
                            .addComponents(ActionRow.of(
                                    Button.secondary("stoicyzm", "1\uFE0F⃣"),
                                    Button.secondary("hedonizm", "2\uFE0F⃣ "),
                                    Button.secondary("materializm", "3\uFE0F⃣ "),
                                    Button.secondary("racjonalizm", "4\uFE0F⃣"),
                                    Button.secondary("egzystencjonalizm", "5️⃣")
                            ))
                            .send(event.getChannel());

                    EmbedBuilder parties1 = new EmbedBuilder()
                            .setTitle("● Filozofie - Part 2")
                            .setDescription("""
                                    :one: Pozytywizm -  Doktryna postulująca, że jedynie naukowo uzasadnione i potwierdzone fakty powinny stanowić podstawę poznania.
                                    :two: Historyzm - Ideolgoia uznająca historyczny kontekst za kluczowy dla zrozumienia danej myśli.
                                    :three: Humanizm - Nurt kładący nacisk na badanie godności i swobodnego rozwoju człowieka w różnych środowiskach, kulturowym, społecznym i naturalnym.
                                    :four: Utylitaryzm - Nurt kładący nacisk na zwiększenie ogólnego dobra lub szczęścia jako główny cel moralnych działań, w tym systemie działanie jest dobre, jeśli prowadzi do maksymilizacji korzyści dla większej liczby ludzi.
                                    """)
                            .setColor(Color.decode("#B32DFF"));

                    new MessageBuilder().setEmbed(parties1)
                            .addComponents(ActionRow.of(
                                    Button.secondary("pozytywizm", "1\uFE0F⃣"),
                                    Button.secondary("historyzm", "2\uFE0F⃣ "),
                                    Button.secondary("humanizm", "3\uFE0F⃣ "),
                                    Button.secondary("utylitaryzm", "4\uFE0F⃣")
                            ))
                            .send(event.getChannel());


                    EmbedBuilder end = new EmbedBuilder()
                            .setTitle("● Wybierz wszystkie swoje ideologie filozoficzne")
                            .setColor(Color.decode("#B32DFF"))
                            .setFooter(event.getServer().get().getName() + " | By Hexlin 2024")
                            .setThumbnail("https://i.imgur.com/XuIIA1v.png")
                            .setImage("https://i.imgur.com/igea9Nk.png")
                            .setTimestamp(OffsetDateTime.now().toInstant());

                    new MessageBuilder().setEmbed(end).send(event.getChannel());
                }
            }
        }
    }

    private void levelUp(User user, Server server, String roleId, TextChannel textChannel) {
        Role role = instance.api.getRoleById(roleId).get();
        if (!user.getRoles(server).contains(role)) {
            user.addRole(role);
            EmbedBuilder congratulations = new EmbedBuilder()
                    .setTitle("● Osiągnięto nowy poziom")
                    .setDescription("\uD83C\uDF89 Gratulacje użytkowniku " + user.getMentionTag() + " awansowałeś na rolę <@&" + roleId + ">.")
                    .setColor(Color.decode("#B32DFF"))
                    .setFooter(server.getName() + " | By Hexlin 2024")
                    .setThumbnail(user.getAvatar().getUrl().toString())
                    .setTimestamp(OffsetDateTime.now().toInstant());
            textChannel.sendMessage(congratulations);
        }
    }

    public boolean isImageURL(String url) {
        return url.endsWith(".png") || url.endsWith(".jpg") && !url.contains("imgur");
    }
}
