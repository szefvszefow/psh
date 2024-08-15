package pl.hexlin.task;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import pl.hexlin.Instance;
import pl.hexlin.rss.Feed;
import pl.hexlin.rss.FeedMessage;
import pl.hexlin.rss.RSSFeedParser;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class RSSTask2 extends TimerTask {
    public final Instance instance;
    public ArrayList<FeedMessage> moscowlatestFeed;

    public RSSTask2(Instance instance) {
        this.instance = instance;
        this.moscowlatestFeed = new ArrayList<>();
    }

    @Override
    public void run() {
        moscowlatestFeed.clear();
        RSSFeedParser parser = new RSSFeedParser(
                "https://www.themoscowtimes.com/rss/news");
        Feed feed = parser.readFeed();
        for (FeedMessage message : feed.getMessages()) {
            moscowlatestFeed.add(message);
        }

        FeedMessage latestFeedMessage = moscowlatestFeed.get(0);

        TextChannel textChannel = instance.api.getTextChannelById("1199454263219343400").get();
        boolean containsLink = false;

        try {
            for (Message message : textChannel.getMessages(20).get()) {
                if (message.getContent().contains(latestFeedMessage.getLink())) {
                    containsLink = true;
                    break;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        if (!containsLink) {
            EmbedBuilder news = new EmbedBuilder()
                    .setTitle("Pojawił się nowy artykuł ● The Moscow Times")
                    .setColor(Color.decode("#B32DFF"))
                    .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy" + " | By Hexlin 2024", "https://odishabytes.com/wp-content/uploads/2023/11/Moscow-rimes-foreign-agent.jpg")
                    .setTimestamp(OffsetDateTime.now().toInstant());


            instance.api.getTextChannelById("1199454263219343400").get().sendMessage(news);
            instance.api.getTextChannelById("1199454263219343400").get().sendMessage(latestFeedMessage.getLink());
        }
    }
}
