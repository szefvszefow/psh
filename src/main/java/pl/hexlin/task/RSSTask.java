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

public class RSSTask extends TimerTask {
    public final Instance instance;
    public ArrayList<FeedMessage> latestFeed;

    public RSSTask(Instance instance) {
        this.instance = instance;
        this.latestFeed = new ArrayList<>();
    }

    @Override
    public void run() {
        latestFeed.clear();
        RSSFeedParser parser = new RSSFeedParser(
                "https://wydarzenia.interia.pl/kraj/feed");
        Feed feed = parser.readFeed();
        System.out.println(feed.getEnclosure());
        for (FeedMessage message : feed.getMessages()) {
            latestFeed.add(message);
        }

        FeedMessage latestFeedMessage = latestFeed.get(0);

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
                    .setTitle("Pojawił się nowy artykuł ● Interia")
                    .setColor(Color.decode("#B32DFF"))
                    .setFooter("\uD83E\uDD85PSH - Serwer Polityczno-Gamingowy" + " | By Hexlin 2024", "https://upload.wikimedia.org/wikipedia/en/thumb/d/d1/Interia.svg/2560px-Interia.svg.png")
                    .setTimestamp(OffsetDateTime.now().toInstant());


            instance.api.getTextChannelById("1199454263219343400").get().sendMessage(news);
            instance.api.getTextChannelById("1199454263219343400").get().sendMessage(latestFeedMessage.getLink());
        }
    }
}
