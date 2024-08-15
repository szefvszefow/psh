package pl.hexlin.task;

import org.javacord.api.entity.emoji.CustomEmoji;
import pl.hexlin.Instance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TimerTask;

public class GoidaTask extends TimerTask {
    public final Instance instance;

    public GoidaTask(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        LocalTime currentTime = LocalTime.now();
        DayOfWeek currentDay = LocalDate.now().getDayOfWeek();

        if (currentTime.getHour() == 12 && currentTime.getMinute() == 00 && currentTime.getSecond() == 0 && !currentDay.equals(DayOfWeek.FRIDAY)) {
            CustomEmoji emoji_r04 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1225045479323209838").get();
            CustomEmoji emoji_r05 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1221692188996403250").get();
            instance.api.getTextChannelById("1199449402243301567").get().sendMessage("Wybiła godzina **12:00** nastąpił czas wielkiej **GOIDY** <:trollfacerussia:1225045479323209838> <a:peeporussia:1221692188996403250>");
            instance.api.getTextChannelById("1199449402243301567").get().sendMessage("http://hexlin.rsbmw.pl/goida.mp4").thenAccept(message -> {
                message.addReaction("\uD83C\uDDEC");
                message.addReaction("\uD83C\uDDF4");
                message.addReaction("\uD83C\uDDEE");
                message.addReaction("\uD83C\uDDE9");
                message.addReaction("\uD83C\uDDE6");
                message.addReaction(emoji_r04);
                message.addReaction(emoji_r05);
            });
        }

        if (currentTime.getHour() == 12 && currentTime.getMinute() == 00 && currentTime.getSecond() == 0 && currentDay.equals(DayOfWeek.FRIDAY)) {
            CustomEmoji emoji_r04 = instance.api.getServerById("1199446281840492629").get().getCustomEmojiById("1225045479323209838").get();
            instance.api.getTextChannelById("1199449402243301567").get().sendMessage("<:like:1200114053469708328> **Dzisiaj jest piątek!** Ważna informacja od Prezydenta Federacji Rosyjskiej. \nhttps://x.com/olgabazova/status/1773780053668594168?s=46").thenAccept(message -> {
                message.addReaction(emoji_r04);
            });
        }
    }
}
