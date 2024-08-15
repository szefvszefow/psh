package pl.hexlin;


import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import pl.hexlin.finance.InvestmentManager;
import pl.hexlin.gfx.GfxProcessManager;
import pl.hexlin.gfx.GraphicsCreator;
import pl.hexlin.handler.*;
import pl.hexlin.http.ApiHandler;
import pl.hexlin.quest.QuestHandler;
import pl.hexlin.question.QuestionManager;
import pl.hexlin.quiz.PoliticalQuizManager;
import pl.hexlin.quiz.QuizManager;
import pl.hexlin.radio.RadioBrowserManager;
import pl.hexlin.radio.TrackPlayer;
import pl.hexlin.radio.station.Station;
import pl.hexlin.radio.station.StationManager;
import pl.hexlin.servermember.ServerMember;
import pl.hexlin.servermember.ServerMemberManager;
import pl.hexlin.task.*;
import pl.hexlin.voting.ElectionManager;
import pl.hexlin.voting.SuggestionManager;
import pl.hexlin.webhook.WebhookManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Instance {
    public DiscordApi api;
    public TrackPlayer trackPlayer;
    public RadioBrowserManager radioBrowserManager;
    public HashMap<Long, Integer> verificationMap;
    public MongoCollection<Document> userCollection;
    public MongoCollection<Document> suggestionCollection;
    public MongoCollection<Document> electionCollection;
    public MongoCollection<Document> questionCollection;
    public MongoCollection<Document> investmentCollection;
    public ServerMemberManager serverMemberManager;
    public ArrayList<ServerMember> topMessagesMap;
    public SuggestionManager suggestionManager;
    public ElectionManager representativeElectionManager;
    public Timer timer;
    public WebhookManager webhookManager;
    public GraphicsCreator graphicsCreator;
    public GfxProcessManager gfxProcessManager;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(7);
    public QuestionManager questionManager;
    public QuizManager quizManager;
    public PoliticalQuizManager politicalQuizManager;
    public ApiHandler apiHandler;
    public QuestHandler questHandler;
    public String questChannelId;
    public StationManager stationManager;
    public InvestmentManager investmentManager;

    public Instance() {
    }

    public static void main(String[] args) {
        new Instance().innit();
    }

    public void innit() {
        Locale.setDefault(new Locale("pl", "PL"));
        this.api = new DiscordApiBuilder()
                .setToken("NzIzMTQzMjkzNDQ1MjEwMTky.GxOldY.l0yZUMW2J5Io1G2D7IgtGNmECpWZnNR4E2tTBk")
                .addIntents(Intent.MESSAGE_CONTENT, Intent.GUILD_MEMBERS)
                .addSlashCommandCreateListener(new SlashCommandHandler(this))
                .addMessageCreateListener(new MessageReceivedHandler(this, new Random()))
                .addMessageComponentCreateListener(new ComponentInteractionHandler(this))
                .addModalSubmitListener(new ModalSubmitHandler(this))
                .addServerMemberJoinListener(new ServerJoinHandler(this))
                .addServerMemberLeaveListener(new ServerLeaveHandler(this))
                .login().join();

        CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(x -> x.hosts(
                        Collections.singletonList(new ServerAddress("136.243.156.104", 27017))))
                .credential(MongoCredential.createCredential("", "", "H@selko7".toCharArray()))
                        .applyToSocketSettings(builder -> {
                            builder.connectTimeout(5000, TimeUnit.MILLISECONDS);
                            builder.readTimeout(5000, TimeUnit.MILLISECONDS);
                        })
                .build());

        MongoDatabase mongoDatabase = mongoClient.getDatabase("mo17555_hexrynek");
        userCollection = mongoDatabase.getCollection("psh").withCodecRegistry(pojoCodecRegistry);
        suggestionCollection = mongoDatabase.getCollection("psh-suggestions").withCodecRegistry(pojoCodecRegistry);
        electionCollection = mongoDatabase.getCollection("psh-elections").withCodecRegistry(pojoCodecRegistry);
        questionCollection = mongoDatabase.getCollection("psh-questions").withCodecRegistry(pojoCodecRegistry);
        investmentCollection = mongoDatabase.getCollection("psh-investments").withCodecRegistry(pojoCodecRegistry);


        this.trackPlayer = new TrackPlayer(this);

        this.verificationMap = new HashMap<>();
        this.serverMemberManager = new ServerMemberManager(this);

        this.topMessagesMap = new ArrayList<>();
        topMessagesMap.addAll(getServerMemberManager().getTopUsersByMessagesSent(4));

        radioBrowserManager = new RadioBrowserManager();

        this.suggestionManager = new SuggestionManager(this);
        this.representativeElectionManager = new ElectionManager(this);

        this.webhookManager = new WebhookManager(this);
        this.graphicsCreator = new GraphicsCreator(this);
        this.gfxProcessManager = new GfxProcessManager();
        this.questionManager = new QuestionManager(this);
        this.quizManager = new QuizManager();
        this.politicalQuizManager = new PoliticalQuizManager();
        this.questChannelId = "1199449402243301567";
        this.apiHandler = new ApiHandler(this);
        this.questHandler = new QuestHandler(this);

        this.stationManager = new StationManager();
        this.stationManager.addStation("kaszebe", new Station("kaszebe", "https://stream3.nadaje.com/8048/stream/1/"));

        SlashCommand playCommand = SlashCommand.with("play", "Odtwarzanie muzyki z YouTube")
                .addOption(SlashCommandOption.createStringOption("link", "Link do muzyki", true))
                .createGlobal(api)
                .join();

        SlashCommand stopCommand = SlashCommand.with("stop", "Zatrzymuje jakiekolwiek odtwarzanie")
                .createGlobal(api)
                .join();

        SlashCommand skipCommand = SlashCommand.with("skip", "Pomija utwór")
                .createGlobal(api)
                .join();

        SlashCommand profileCommand = SlashCommand.with("profil", "Przejrzyj swoje statystyki")
                .addOption(SlashCommandOption.createMentionableOption("user", "(Opcjonalnie) Użytkownik którego profil chcesz sprawdzić", false))
                .createGlobal(api)
                .join();

        SlashCommand setStatusCommand = SlashCommand.with("wizytowka", "Możliwość ustawienia swojej serwerowej wizytówki która jest widoczna jak ktoś Cię spinguje.")
                .addOption(SlashCommandOption.createStringOption("content", "Tresć wizytówki", true))
                .addOption(SlashCommandOption.createStringOption("image", "Link do obrazka (.png i .jpg) (tylko odpowiednie obrazki).", true))
                .createGlobal(api)
                .join();

        SlashCommand radioCommand = SlashCommand.with("radio", "Włącz odtwarzanie radia")
                .addOption(SlashCommandOption.createStringOption("name", "Nazwa radia", true))
                .createGlobal(api)
                .join();

        SlashCommand setMessagesCommand = SlashCommand.with("ustawwiadomosci", "Ustaw wiadomości użytkownikowi (Tylko Administracja)")
                .addOption(SlashCommandOption.createMentionableOption("uzytkownik", "Użytkownik", true))
                .addOption(SlashCommandOption.createDecimalOption("ilosc", "Ilość", true))
                .createGlobal(api)
                .join();

        SlashCommand blockCommand = SlashCommand.with("zablokujuzytkownika", "Wyślij użytkownika na mute ktory może usunąć tylko właściciel. (Tylko Administracja)")
                .addOption(SlashCommandOption.createMentionableOption("uzytkownik", "Użytkownik", true))
                .createGlobal(api)
                .join();

        SlashCommand stockCommand = SlashCommand.with("notowania", "Sprawdź cenę akcji wybranej korporacji, możesz w nią zainwestowąć korzystając z waluty serwerowej.")
                .addOption(SlashCommandOption.createStringOption("tag", "TAG Giełdowy np. TSLA", true))
                .createGlobal(api)
                .join();

        scheduleTaskAsync(new ElectionTask(this), 1000, 1000);
        scheduleTaskAsync(new SuggestionTask(this), 1000, 1000);
        scheduleTaskAsync(new ToplistTask(this), 1000, 600000);
        scheduleTaskAsync(new RSSTask(this), 1000, 60000);
        scheduleTaskAsync(new RSSTask2(this), 1000, 60000);
        scheduleTaskAsync(new PresenceUpdateTask(this), 1000, 60000);
        scheduleTaskAsync(new TopMoneyTask(this), 1000, 5000);
        scheduleTaskAsync(new GoidaTask(this), 1000, 1000);
        scheduleTaskAsync(new QuestTask(this), 0, 900);
        scheduleTaskAsync(new BlockTask(this), 0, 1000);
    }

    public CompletableFuture<BufferedImage> applyWatermark(Attachment attachment) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                BufferedImage originalImage = ImageIO.read(new URL(attachment.getUrl().toString()));
                BufferedImage watermarkImage = ImageIO.read(new File("/root/hexszon/watermark.png"));

                Graphics2D graphics = originalImage.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int scaledWidth = originalImage.getWidth() / 3;
                int scaledHeight = (scaledWidth * watermarkImage.getHeight()) / watermarkImage.getWidth();

                int x = originalImage.getWidth() - scaledWidth - 10;
                int y = originalImage.getHeight() - scaledHeight - 10;

                graphics.drawImage(watermarkImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH), x, y, null);
                graphics.dispose();
                return originalImage;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void scheduleTaskAsync(Runnable task, long initialDelay, long period) {
        CompletableFuture.runAsync(() -> {
            scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
        });
    }

    public int parseAxis(int axis) {
        if (axis < 0) {
            return 0;
        } else if (axis > 100) {
            return 100;
        } else {
            return axis;
        }
    }
    public int calculatePercentage(int part, int total) {
        if (total == 0) {
            throw new IllegalArgumentException("Total cannot be zero.");
        }
        double percentage = ((double) part / total) * 100;
        if (percentage < 0) {
            return 0;
        } else if (percentage > 100) {
            return 100;
        } else {
            return (int) percentage;
        }
    }

    public InvestmentManager getInvestmentManager() {
        return investmentManager;
    }

    public ApiHandler getApiHandler() {
        return apiHandler;
    }

    public QuestHandler getQuestHandler() {
        return questHandler;
    }

    public QuestionManager getQuestionManager() {
        return questionManager;
    }

    public GfxProcessManager getGfxProcessManager() {
        return gfxProcessManager;
    }

    public WebhookManager getWebhookManager() {
        return webhookManager;
    }

    public ServerMemberManager getServerMemberManager() {
        return serverMemberManager;
    }

    public SuggestionManager getSuggestionManager() {
        return suggestionManager;
    }

    public QuizManager getQuizManager() {
        return quizManager;
    }

    public StationManager getStationManager() {
        return stationManager;
    }

    public String getServerRoleById(String Id) {
        switch (Id) {
            case "pedalisation" -> {
                return "1224727594704965744";
            }
            case "banderism" -> {
                return "1224727168240848997";
            }
            case "alcoholism" -> {
                return "1224726394253348904";
            }
            case "pings" -> {
                return "1217858926792675449";
            }
            case "poland" -> {
                return "1215029923975012423";
            }
            case "abroad" -> {
                return "1215030237071409203";
            }
            case "convervatism" -> {
                return "1199828160003448973";
            }
            case "traditionalism" -> {
                return "1199828166953410711";
            }
            case "nationalism" -> {
                return "1199828165502193764";
            }
            case "monarchism" -> {
                return "1199828168073285653";
            }
            case "christian-democracy" -> {
                return "1199828169172209784";
            }
            case "national-democracy" -> {
                return "1199828170560503898";
            }
            case "teocracy" -> {
                return "1199828171802034287";
            }
            case "paleolibertarianism" -> {
                return "1199828172938674206";
            }
            case "classical-liberalism" -> {
                return "1199828174092107866";
            }
            case "representative-democracy" -> {
                return "1199828174956138551";
            }
            case "direct-democracy" -> {
                return "1199828177091051600";
            }
            case "korwinism" -> {
                return "1199828178521301032";
            }
            case "anti-communism" -> {
                return "1199828179548897361";
            }
            case "euro-scepticism" -> {
                return "1199828181109194895";
            }
            case "antisyonizm" -> {
                return "1199828183084712147";
            }
            case "anti-lgbt" -> {
                return "1199828184640802876";
            }
            case "reactionism" -> {
                return "1199828185907466391";
            }
            case "pro-life" -> {
                return "1199828187211898880";
            }
            case "fascism" -> {
                return "1199828188868661388";
            }
            case "national-socialism" -> {
                return "1199828190147907634";
            }
            case "falangism" -> {
                return "1199828191389417512";
            }
            case "white-supremacy" -> {
                return "1199828192614154320";
            }
            case "militarism" -> {
                return "1199828194237349959";
            }
            case "corportaism" -> {
                return "1199828195755696219";
            }
            case "capitalism" -> {
                return "1199828197446000721";
            }
            case "leseferism" -> {
                return "1199828198767210576";
            }
            case "acap" -> {
                return "1199828200142938282";
            }
            case "market" -> {
                return "1199828201803890788";
            }
            case "socialliberalism" -> {
                return "1199828203649372160";
            }
            case "welfare-state" -> {
                return "1199838044199592066";
            }
            case "democratic-socialism" -> {
                return "1199828205096403054";
            }
            case "socialdemocracy" -> {
                return "1199828206627332106";
            }
            case "internationalism" -> {
                return "1199828207776563301";
            }
            case "progress" -> {
                return "1199828209508827196";
            }
            case "pro-choice" -> {
                return "1199828210993602690";
            }
            case "cosmopolitism" -> {
                return "1199828212616798289";
            }
            case "pro-eu" -> {
                return "1199828213841543281";
            }
            case "anti-gun" -> {
                return "1199828215385034762";
            }
            case "socialism" -> {
                return "1199828216546869269";
            }
            case "feminism" -> {
                return "1199828217717071942";
            }
            case "pro-lgbt" -> {
                return "1199828219046666380";
            }
            case "marxism" -> {
                return "1199828220271398913";
            }
            case "communism" -> {
                return "1199828221747810464";
            }
            case "leninism" -> {
                return "1199828222821548092";
            }
            case "stalinism" -> {
                return "1199829608170795070";
            }
            case "national-bolshevism" -> {
                return "1199829609844322344";
            }
            case "trotskyism" -> {
                return "1199829610909663372";
            }
            case "anarchism" -> {
                return "1199829612075687996";
            }
            case "pirate" -> {
                return "1199829710927056976";
            }
            case "centre" -> {
                return "1199839176179339475";
            }
            case "protectionism" -> {
                return "1199829711929487501";
            }
            case "distributionism" -> {
                return "1199829713288429701";
            }
            case "malpishonism" -> {
                return "1199829714504777829";
            }
            case "z-ideology" -> {
                return "1199829715821797558";
            }
            case "femboyism" -> {
                return "1199829821765730454";
            }
            case "that-dependyism" -> {
                return "1199829823594430625";
            }
            case "unfonism" -> {
                return "1199829825347661954";
            }
            case "pis" -> {
                return "1199839916906004571";
            }
            case "confederacy" -> {
                return "1199839920156581998";
            }
            case "left" -> {
                return "1199839921356152833";
            }
            case "3d" -> {
                return "1199839923151319121";
            }
            case "ko" -> {
                return "1199839924178911272";
            }
            case "male" -> {
                return "1199831705821904916";
            }
            case "female" -> {
                return "1199831711740076112";
            }
            case "femboy" -> {
                return "1199831714281816094";
            }
            case "other" -> {
                return "1199831717037490216";
            }
            case "hoi4" -> {
                return "1199831728974475344";
            }
            case "aoh" -> {
                return "1199831726407549070";
            }
            case "other-paradox" -> {
                return "1199832351803445340";
            }
            case "war-thunder" -> {
                return "1199831727879766027";
            }
            case "proru" -> {
                return "1199856672961740990";
            }
            case "proua" -> {
                return "1199856684181487626";
            }
            case "net" -> {
                return "1199856688564543518";
            }
            case "nato" -> {
                return "1199856689613123594";
            }
            case "anti-nato" -> {
                return "1199856690426818631";
            }
            case "obywatel" -> {
                return "1199882887328190534";
            }
            case "urzędnik" -> {
                return "1199884716443193424";
            }
            case "minister" -> {
                return "1199882943296966666";
            }
            case "poseł" -> {
                return "1199882944320381049";
            }
            case "cesarz" -> {
                return "1199882945427677365";
            }
            case "oligarcha" -> {
                return "1199885331017760919";
            }
            case "libertarianism" -> {
                return "1200292109975035913";
            }
            case "egalitarism" -> {
                return "1200292111019425824";
            }
            case "ecologism" -> {
                return "1200292111929581640";
            }
            case "syndicalism" -> {
                return "1200292113468882945";
            }
            case "authoritarism" -> {
                return "1200292115155009699";
            }
            case "paneuropeism" -> {
                return "1200292116690116748";
            }
            case "panslovianism" -> {
                return "1200292118149734400";
            }
            case "totalitarism" -> {
                return "1200292119168946317";
            }
            case "liberalism" -> {
                return "1200293175735095456";
            }

            case "stoicyzm" -> {
                return "1201474629252947988";
            }

            case "hedonizm" -> {
                return "1201474630582538402";
            }

            case "materializm" -> {
                return "1201474632058949632";
            }

            case "racjonalizm" -> {
                return "1201474633304649838";
            }

            case "egzystencjalizm" -> {
                return "1201474634663592038";
            }

            case "pozytywizm" -> {
                return "1201474635951247502";
            }

            case "historyzm" -> {
                return "1201474636966273056";
            }

            case "humanizm" -> {
                return "1201474639721922570";
            }
            case "utylitaryzm" -> {
                return "1201474820026663024";
            }
            case "religious" -> {
                return "1201489653971304458";
            }
            case "ateist" -> {
                return "1201489657129607168";
            }
            case "agnostic" -> {
                return "1201489659990134864";
            }
            case "ksh" -> {
                return "1202946163498811452";
            }
            case "sil" -> {
                return "1202946318683742208";
            }
            case "Dolnośląskie" -> {
                return "1202947807602802759";
            }
            case "Kujawsko-pomorskie" -> {
                return "1202947809238847509";
            }
            case "Lubelskie" -> {
                return "1202947810572369951";
            }
            case "Lubuskie" -> {
                return "1202947811646382080";
            }
            case "Łódzkie" -> {
                return "1202947812912795689";
            }
            case "Małopolskie" -> {
                return "1202947815014408282";
            }
            case "Mazowieckie" -> {
                return "1202947817551962123";
            }
            case "Opolskie" -> {
                return "1202947820252954684";
            }
            case "Podkarpackie" -> {
                return "1202947823331446844";
            }
            case "Podlaskie" -> {
                return "1202947824677945364";
            }
            case "Pomorskie" -> {
                return "1202947825680523304";
            }
            case "Śląskie" -> {
                return "1202947827064381470";
            }
            case "Świętokrzyskie" -> {
                return "1202947828519936080";
            }
            case "Warmińsko-Mazurskie" -> {
                return "1202947830315094037";
            }
            case "Wielkopolskie" -> {
                return "1202947831816523806";
            }
            case "Zachodniopomorskie" -> {
                return "1202947833192386570";
            }
        }
        return null;
    }
    public String convertTime(long time) {
        time -= System.currentTimeMillis();
        if (time <= 0L) {
            return " < 0s";
        }
        final StringBuilder stringBuilder = new StringBuilder();
        final long days = time / 86400000L;
        final long hours = time / 3600000L % 24L;
        final long minutes = time / 60000L % 60L;
        final long seconds = time / 1000L % 60L;
        final long milis = time % 1000L;
        if (days > 0L) {
            stringBuilder.append(days).append("d");
        }
        if (hours > 0L) {
            stringBuilder.append(hours).append("h");
        }
        if (minutes > 0L) {
            stringBuilder.append(minutes).append("min");
        }
        if (seconds > 0L) {
            stringBuilder.append(seconds).append("s");
        }
        if (days == 0L && hours == 0L && minutes == 0L && seconds == 0L && milis > 0L) {
            stringBuilder.append(milis).append("ms");
        }
        return stringBuilder.toString();
    }
}