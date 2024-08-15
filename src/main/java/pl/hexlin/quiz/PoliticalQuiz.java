package pl.hexlin.quiz;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.user.User;
import pl.hexlin.Instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoliticalQuiz {
    public final Instance instance;
    public final String userId;
    public final String messageId;

    public int socialistAxe = 0;
    public int capitalistAxe = 0;
    public int liberalismAxe = 0;
    public int authoritarianAxe = 0;
    public int nationalismAxe = 0;
    public int internationalismAxe = 0;
    public int religionAxe = 0;
    public int traditionalismAxe = 0;
    public int militarismAxe = 0;
    public int progressAxe = 0;

    public int currentStage;
    public int convervatismScore = 0;
    public int traditionalismScore = 0;
    public int nationalismScore = 0;
    public int monarchismScore = 0;
    public int christianDemocracyScore = 0;
    public int nationalDemocracyScore = 0;
    public int teocracyScore = 0;
    public int paleolibertarianismScore = 0;
    public int classicalLiberalismScore = 0;
    public int representativeDemocracyScore = 0;
    public int directDemocracyScore = 0;
    public int korwinismScore = 0;
    public int antiCommunismScore = 0;
    public int euroScepticismScore = 0;
    public int antisyonizmScore = 0;
    public int antiLgbtScore = 0;
    public int reactionismScore = 0;
    public int proLifeScore = 0;
    public int fascismScore = 0;
    public int nationalSocialismScore = 0;
    public int falangismScore = 0;
    public int whiteSupremacyScore = 0;
    public int militarismScore = 0;
    public int corportaismScore = 0;
    public int capitalismScore = 0;
    public int leseferismScore = 0;
    public int acapScore = 0;
    public int marketScore = 0;
    public int socialliberalismScore = 0;
    public int welfareStateScore = 0;
    public int democraticSocialismScore = 0;
    public int socialdemocracyScore = 0;
    public int internationalismScore = 0;
    public int progressScore = 0;
    public int proChoiceScore = 0;
    public int cosmopolitismScore = 0;
    public int proEuScore = 0;
    public int antiGunScore = 0;
    public int socialismScore = 0;
    public int feminismScore = 0;
    public int proLgbtScore = 0;
    public int marxismScore = 0;
    public int communismScore = 0;
    public int leninismScore = 0;
    public int stalinismScore = 0;
    public int nationalBolshevismScore = 0;
    public int trotskyismScore = 0;
    public int anarchismScore = 0;
    public int pirateScore = 0;
    public int centreScore = 0;
    public int protectionismScore = 0;
    public int distributionismScore = 0;
    public int authoritarismScore = 0;

    public ArrayList<String> predictedIdeologies;
    public HashMap<String, String> translatedIdeologies;
    public HashMap<String, Integer> scoresMap;
    public ArrayList<String> questionsList;
    public PoliticalQuiz(Instance instance, String userId, String messageId, int currentStage) {
        this.instance = instance;
        this.userId = userId;
        this.messageId = messageId;
        this.currentStage = currentStage;
        this.predictedIdeologies = new ArrayList<>();
        this.scoresMap = new HashMap<>();
        this.questionsList = new ArrayList<>();
        this.translatedIdeologies = new HashMap<>();

        this.questionsList.addAll(List.of("Czy uważasz progres techniczny jako dobry?",//0
                "Czy popierasz silne przywództwo narodowe?", //1
                "Czy uważasz, że kraje powinny skupić się bardziej na kwestiach krajowych niż międzynarodowych?",//2
                "Czy uważasz, że tradycyjne wartości społeczne powinny być zachowywane?", //3
                "Czy popierasz ograniczone zmiany w społeczeństwie?", //4
                "Czy uważasz, że tradycje powinny być głównym filarem społeczeństwa?", //5
                "Czy popierasz monarchię jako formę rządów?", //6
                "Czy popierasz integrację wartości chrześcijańskich z demokratycznymi zasadami?", //7
                "Czy popierasz demokrację opartą na krajowych interesach?", //8
                "Czy popierasz rządy oparte na zasadach religijnych?", //9
                "Czy popierasz minimalną ingerencję państwa w życie obywateli?", //10
                "Czy uważasz, że wolność jednostki powinna być nadrzędnym priorytetem?", //#11
                "Czy popierasz system reprezentacyjnej demokracji?", //#12
                "Czy popierasz bezpośredni udział obywateli w procesach decyzyjnych?", //#13
                "Czy popierasz idee ekstremalnego liberalizmu gospodarczego?", //#14
                "Czy popierasz sprzeciw wobec ideologii komunistycznej?", //#15
                "Czy uważasz, że komunizm stanowi zagrożenie dla społeczeństwa?", //#16
                "Czy uważasz, że państwa powinny zachować większą niezależność wobec Unii Europejskiej?", //#17
                "Czy popierasz krytyczne podejście do integracji europejskiej?", //#18
                "Czy popierasz ograniczenia praw osób LGBTQ+?", //#19
                "Czy uważasz, że społeczeństwo powinno być oparte na tradycyjnych normach dotyczących płci i seksualności?", //#20
                "Czy popierasz ochronę życia poczętego?", //!21
                "Czy uważasz, że aborcja powinna być całkowicie zakazana?", //!22
                "Czy popierasz autorytarne rządy i silną kontrolę państwa?", //!23
                "Czy uważasz, że naród powinien być jednorodny etnicznie?", //!24
                "Czy popierasz budowę silnego wojska i aktywną politykę zagraniczną?", //!25
                "Czy popierasz łączenie interesów państwa, pracowników i korporacji?", //!26
                "Czy popierasz wolny rynek i własność prywatną?", //!27
                "Czy uważasz, że rynek sam się reguluje i nie potrzebuje interwencji państwa?", //!28
                "Czy popierasz zniesienie wszelkich form podatków?", //!29
                "Czy popierasz swobodę handlu i konkurencji?", //!30
                "Czy uważasz, że państwo powinno zapewniać pewne świadczenia socjalne dla obywateli?", //$31
                "Czy popierasz społeczeństwo oparte na demokracji politycznej i kontroli społecznej nad środkami produkcji?", //$32
                "Czy uważasz, że państwo powinno zarządzać kluczowymi sektorami gospodarki?", //$33
                "Czy popierasz współpracę międzynarodową w celu rozwiązania globalnych problemów?", //$34
                "Czy uważasz, że państwo powinno działać na rzecz redukcji nierówności społecznych?", //$35
                "Czy uważasz, że kobiety powinny mieć pełną kontrolę nad swoim ciałem?", //$36
                "Czy popierasz otwarcie się na różnorodność kulturową i społeczną?", //$37
                "Czy popierasz dalszą integrację europejską?", //$38
                "Czy popierasz ograniczenia w dostępie do broni palnej?", //$39
                "Czy popierasz równość płci i walkę z dyskryminacją na podstawie płci?", //$40
                "Czy popierasz prawa osób LGBTQ+ i walkę z homofobią?", //41
                "Czy popierasz walkę klasową i przekształcenie społeczeństwa w kluczowej rewolucji proletariackiej?", //42
                "Czy popierasz społeczeństwo bezklasowe, gdzie środki produkcji są wspólną własnością?", //43
                "Czy popierasz połączenie socjalizmu z kapitalizmem?")); //44

        scoresMap.put("convervatism", convervatismScore);
        scoresMap.put("traditionalism", traditionalismScore);
        scoresMap.put("nationalism", nationalismScore);
        scoresMap.put("monarchism", monarchismScore);
        scoresMap.put("christian-democracy", christianDemocracyScore);
        scoresMap.put("national-democracy", nationalDemocracyScore);
        scoresMap.put("teocracy", teocracyScore);
        scoresMap.put("paleolibertarianism", paleolibertarianismScore);
        scoresMap.put("classical-liberalism", classicalLiberalismScore);
        scoresMap.put("representative-democracy", representativeDemocracyScore);
        scoresMap.put("direct-democracy", directDemocracyScore);
        scoresMap.put("korwinism", korwinismScore);
        scoresMap.put("anti-communism", antiCommunismScore);
        scoresMap.put("euro-scepticism", euroScepticismScore);
        scoresMap.put("antisyonizm", antisyonizmScore);
        scoresMap.put("anti-lgbt", antiLgbtScore);
        scoresMap.put("reactionism", reactionismScore);
        scoresMap.put("pro-life", proLifeScore);
        scoresMap.put("fascism", fascismScore);
        scoresMap.put("national-socialism", nationalSocialismScore);
        scoresMap.put("falangism", falangismScore);
        scoresMap.put("wwhite-supremacy", whiteSupremacyScore);
        scoresMap.put("militarism", militarismScore);
        scoresMap.put("corporatism", corportaismScore);
        scoresMap.put("capitalism", capitalismScore);
        scoresMap.put("leseferism", leseferismScore);
        scoresMap.put("acap", acapScore);
        scoresMap.put("market", marketScore);
        scoresMap.put("socialliberalism", socialliberalismScore);
        scoresMap.put("welfare-state", welfareStateScore);
        scoresMap.put("democratic-socialism", democraticSocialismScore);
        scoresMap.put("socialdemocracy", socialdemocracyScore);
        scoresMap.put("internationalism", internationalismScore);
        scoresMap.put("progress", progressScore);
        scoresMap.put("pro-choice", proChoiceScore);
        scoresMap.put("cosmopolitism", cosmopolitismScore);
        scoresMap.put("pro-eu", proEuScore);
        scoresMap.put("anti-gun", antiGunScore);
        scoresMap.put("socialism", socialismScore);
        scoresMap.put("feminism", feminismScore);
        scoresMap.put("pro-lgbt", proLgbtScore);
        scoresMap.put("marxism", marxismScore);
        scoresMap.put("communism", communismScore);
        scoresMap.put("leninism", leninismScore);
        scoresMap.put("stalinism", stalinismScore);
        scoresMap.put("national-bolshevism", nationalBolshevismScore);
        scoresMap.put("trotskyism", trotskyismScore);
        scoresMap.put("anarchism", anarchismScore);
        scoresMap.put("pirate", pirateScore);
        scoresMap.put("centre", centreScore);
        scoresMap.put("protectionism", protectionismScore);
        scoresMap.put("distributionism", distributionismScore);
        scoresMap.put("authoritarism", authoritarismScore);

        translatedIdeologies.put("convervatism", "Konserwatyzm");
        translatedIdeologies.put("traditionalism", "Tradycjonalizm");
        translatedIdeologies.put("nationalism", "Nacjonalizm");
        translatedIdeologies.put("monarchism", "Monarchizm");
        translatedIdeologies.put("christian-democracy", "Chrześcijańska-Demokracja");
        translatedIdeologies.put("national-democracy", "Narodowa-Demokracja");
        translatedIdeologies.put("teocracy", "Teokracja");
        translatedIdeologies.put("paleolibertarianism", "Paleolibertarianizm");
        translatedIdeologies.put("classical-liberalism", "Klasyczny-Liberalizm");
        translatedIdeologies.put("representative-democracy", "Demokracja-Reprezentacyjna");
        translatedIdeologies.put("direct-democracy", "Demokracja-Bezpośrednia");
        translatedIdeologies.put("korwinism", "Korwinizm");
        translatedIdeologies.put("anti-communism", "Antykomunizm");
        translatedIdeologies.put("euro-scepticism", "Eurosceptycyzm");
        translatedIdeologies.put("antisyonizm", "Antysyjonizm");
        translatedIdeologies.put("anti-lgbt", "Anty-LGBT");
        translatedIdeologies.put("reactionism", "Reakcjonizm");
        translatedIdeologies.put("pro-life", "Pro-Life");
        translatedIdeologies.put("fascism", "Faszyzm");
        translatedIdeologies.put("national-socialism", "Narodowy-Socjalizm");
        translatedIdeologies.put("falangism", "Falangizm");
        translatedIdeologies.put("white-supremacy", "Biała-Supremacja");
        translatedIdeologies.put("militarism", "Militaryzm");
        translatedIdeologies.put("corporatism", "Korporacjonizm");
        translatedIdeologies.put("capitalism", "Kapitalizm");
        translatedIdeologies.put("leseferism", "Leseferyzm");
        translatedIdeologies.put("acap", "Acap");
        translatedIdeologies.put("market", "Wolny-Rynek");
        translatedIdeologies.put("socialliberalism", "Liberalizm-Społeczny");
        translatedIdeologies.put("welfare-state", "Państwo-Dobrobytu");
        translatedIdeologies.put("democratic-socialism", "Socjalizm-Demokratyczny");
        translatedIdeologies.put("socialdemocracy", "Socjaldemokracja");
        translatedIdeologies.put("internationalism", "Kosmopolityzm");
        translatedIdeologies.put("progress", "Progresywizm");
        translatedIdeologies.put("pro-choice", "Pro-Choice");
        translatedIdeologies.put("cosmopolitism", "Kosmopolityzm");
        translatedIdeologies.put("pro-eu", "Pro-UE");
        translatedIdeologies.put("anti-gun", "Anti-Gun");
        translatedIdeologies.put("socialism", "Socjalizm");
        translatedIdeologies.put("feminism", "Feminizm");
        translatedIdeologies.put("pro-lgbt", "Pro-LGBT");
        translatedIdeologies.put("marxism", "Marksizm");
        translatedIdeologies.put("communism", "Komunizm");
        translatedIdeologies.put("leninism", "Leninizm");
        translatedIdeologies.put("stalinism", "Stalinizm");
        translatedIdeologies.put("national-bolshevism", "Narodowy-Bolszewizm");
        translatedIdeologies.put("trotskyism", "Trockizm");
        translatedIdeologies.put("anarchism", "Anarchizm");
        translatedIdeologies.put("pirate", "Piratyzm");
        translatedIdeologies.put("centre", "Centryzm");
        translatedIdeologies.put("protectionism", "Protekcjonizm");
        translatedIdeologies.put("distributionism", "Dystrybucjonizm");
        translatedIdeologies.put("authoritarism", "Autorytaryzm");
    }

    public void makePredictions() {
        for (Map.Entry<String, Integer> entry : scoresMap.entrySet()) {
            String ideologyName = entry.getKey();
            int currentScore = entry.getValue();

            if (currentScore > 2) {
                predictedIdeologies.add(ideologyName);
            }

            if (currentScore == 2) {
                predictedIdeologies.add(ideologyName);
            }
        }
    }

    public void update(String messageId, TextChannel textChannel) {
        currentStage++;
        makePredictions();
        instance.api.getMessageById(messageId, textChannel).thenAccept(message -> {
            message.createUpdater().removeAllComponents().setContent(questionsList.get(currentStage) + " | " + currentStage + "/44").addComponents(ActionRow.of(Button.success("political-yes", "✅ Tak")), ActionRow.of(Button.secondary("political-neutral", "➖ To zależy")), ActionRow.of(Button.danger("political-no", "❎ Nie"))).applyChanges();
        });
    }

    public void finish(String messageId, TextChannel textChannel, User user) {
        instance.api.getMessageById(messageId, textChannel).thenAccept(message -> {
            StringBuilder resultBuilder = new StringBuilder();
            predictedIdeologies.forEach(predictedIdeology -> {
                resultBuilder.append(translatedIdeologies.get(predictedIdeology)).append(" ");
            });
            String result = resultBuilder.toString().trim();
            message.createUpdater().removeAllComponents().setContent("Dziękujemy za ukończenie testu politycznego! Czy chcesz aby zostały nadane ci poniższe role? \n" + result + "\nJeżeli brakuje jakiejś ideologii albo jest nieprawidłowa, możesz usunąc lub dodać ją na <#1199945690739122258>.")
                    .addComponents(ActionRow.of(Button.success("yes", "✅ Tak")), ActionRow.of(Button.danger("no", "❎ Nie")))
                    .applyChanges();
            try {
                instance.getWebhookManager().sendPoliticalGraphic(user, instance.graphicsCreator.createPoliticalQuizImage(user.getIdAsString(), "politicalquiz.png", socialistAxe, capitalistAxe, liberalismAxe, authoritarianAxe, nationalismAxe, internationalismAxe, religionAxe, traditionalismAxe, militarismAxe, progressAxe));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void addRoles() {
        predictedIdeologies.forEach(predictedIdeology -> {
            instance.api.getServerById("1199446281840492629").get().getMemberById(userId).get().addRole(instance.api.getRoleById(instance.getServerRoleById(predictedIdeology)).get()).join();
        });
    }
}
